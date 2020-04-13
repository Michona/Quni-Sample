package com.qusion.quni.domain

import retrofit2.Response
import java.io.IOException

/**
 * This provides a nice way of catching and reacting to errors and responses from network calls,
 * with as little boilerplate code as possible.
 */
sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val value: T) : NetworkResult<T>()
    data class Error(val cause: Exception? = null, val code: Int? = null) : NetworkResult<Nothing>()
}


/**
 * Transforms Retrofit [Response] to our own [NetworkResult]
 * */
fun <T : Any> Response<T>.toNetworkResult(): NetworkResult<T> {
    val data = this.body()

    return if (data != null && this.isSuccessful) {
        NetworkResult.Success(data)
    } else {
        NetworkResult.Error(code = this.code())
    }
}

inline fun <reified T : Any> safeApiCall(block: () -> NetworkResult<T>): NetworkResult<T> {
    return try {
        block()
    } catch (e: IOException) {
        NetworkResult.Error(cause = e)
    }
}
