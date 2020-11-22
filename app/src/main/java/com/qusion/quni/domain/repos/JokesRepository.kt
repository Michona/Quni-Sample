package com.qusion.quni.domain.repos

import com.quni.apolloservice.JokeQuery
import com.quni.apolloservice.api.IApolloService
import com.quni.apolloservice.api.NetworkResult

class JokesRepository(private val apolloService: IApolloService) {

    /** Calls the API to get a random joke. */
    suspend fun getRemoteRandomJoke(): NetworkResult<JokeQuery.Data> =
        apolloService.safeQuery(query = JokeQuery())
}
