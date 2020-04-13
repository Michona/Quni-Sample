package com.qusion.quni.domain.repos

import com.qusion.quni.data.db.daos.JokesDao
import com.qusion.quni.data.remote.api.JokesApi
import com.qusion.quni.domain.NetworkResult
import com.qusion.quni.domain.entities.JokeDto
import com.qusion.quni.domain.safeApiCall
import com.qusion.quni.domain.toNetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import timber.log.Timber

class JokesRepository(private val jokesApi: JokesApi, private val jokesCache: JokesDao) {

    /**
     * Gets a flow that listens to updates on our local database.
     * Whenever we insert a value, this flow emits a value.
     * */
    fun getCachedRandomJoke(): Flow<JokeDto> = jokesCache.getLatestRandomJoke().filterNotNull()

    /** Calls the API to get a random joke. */
    suspend fun getRemoteRandomJoke() = safeApiCall {
        jokesApi.getRandomJoke().toNetworkResult()
    }
}