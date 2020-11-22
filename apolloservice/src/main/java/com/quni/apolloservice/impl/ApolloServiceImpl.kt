package com.quni.apolloservice.impl

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.*
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.apollographql.apollo.fetcher.ResponseFetcher
import com.quni.apolloservice.BuildConfig
import com.quni.apolloservice.api.BusinessException
import com.quni.apolloservice.api.IApolloService
import com.quni.apolloservice.api.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

internal class ApolloServiceImpl(
    private val context: Context
) : IApolloService {

    @Volatile
    private var client: ApolloClient? = null

    private fun getClient(): ApolloClient {
        return client ?: synchronized(this) {
            client ?: buildApolloClient().also {
                client = it
            }
        }
    }

    override suspend fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables> safeQuery(
        query: Query<D, T, V>,
        cachePolicy: HttpCachePolicy.Policy,
        responseFetcher: ResponseFetcher
    ): NetworkResult<T> {
        return try {
            query(query, cachePolicy, responseFetcher)
        } catch (e: ApolloNetworkException) {
            NetworkResult.Error(cause = e)
        }
    }

    override suspend fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables> safeMutation(
        mutation: Mutation<D, T, V>
    ): NetworkResult<T> {
        return try {
            mutate(mutation)
        } catch (e: ApolloNetworkException) {
            NetworkResult.Error(cause = e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables> flow(
        query: Query<D, T, V>
    ): Flow<NetworkResult<T>> = getClient()
        .query(query)
        .toBuilder()
        .responseFetcher(ApolloResponseFetchers.CACHE_AND_NETWORK)
        .build()
        .toFlow()
        .map {
            if (it.hasErrors()) {
                parseResponseError(it.errors?.first())
            } else {
                NetworkResult.Success(it.data!!)
            }
        }
        .catch { e ->
            emit(NetworkResult.Error(cause = e))
        }
        .flowOn(Dispatchers.IO)

    override fun clearData() {
        getClient().clearNormalizedCache()
    }

    private suspend fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables> query(
        query: Query<D, T, V>,
        cachePolicy: HttpCachePolicy.Policy = HttpCachePolicy.NETWORK_ONLY,
        responseFetcher: ResponseFetcher = ApolloResponseFetchers.NETWORK_ONLY
    ): NetworkResult<T> = withContext(Dispatchers.IO) {

        val response =
            getClient().query(query).toBuilder().httpCachePolicy(cachePolicy)
                .responseFetcher(responseFetcher).build().await()

        if (response.hasErrors()) {
            return@withContext parseResponseError(response.errors?.first())
        }
        NetworkResult.Success(response.data!!)
    }

    private suspend fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables> mutate(
        mutation: Mutation<D, T, V>
    ): NetworkResult<T> = withContext(Dispatchers.IO) {

        val response = getClient().mutate(mutation).await()

        if (response.hasErrors()) {
            return@withContext parseResponseError(response.errors?.first())
        }
        NetworkResult.Success(response.data!!)
    }

    private fun parseResponseError(error: Error?): NetworkResult.Error {
        return NetworkResult.Error(
            cause = BusinessException(
                message = error?.message
            )
        )
    }


    private fun buildApolloClient(): ApolloClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))

        val okHttpClient = okHttpClientBuilder.build()

        val cacheFactory = SqlNormalizedCacheFactory(context, "quni_db")
        val resolver = object : CacheKeyResolver() {
            override fun fromFieldRecordSet(
                field: ResponseField,
                recordSet: Map<String, Any>
            ): CacheKey {
                return if (recordSet.containsKey("id") && recordSet["id"] is String) {
                    CacheKey.from(recordSet["id"] as String)
                } else {
                    CacheKey.NO_KEY
                }
            }

            override fun fromFieldArguments(
                field: ResponseField,
                variables: Operation.Variables
            ): CacheKey {
                return if (field.resolveArgument("id", variables) is String) {
                    CacheKey.from(field.resolveArgument("id", variables) as String)
                } else {
                    CacheKey.NO_KEY
                }
            }
        }

        return ApolloClient.builder()
            .serverUrl(BuildConfig.API_GRAPHQL_URL)
            .okHttpClient(okHttpClient)
            .normalizedCache(cacheFactory, resolver)
            .build()
    }
}
