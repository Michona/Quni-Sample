package com.qusion.quni.data.remote.api

import com.qusion.quni.domain.entities.JokeDto
import retrofit2.Response
import retrofit2.http.GET

interface JokesApi {

    @GET("/")
    suspend fun getRandomJoke(): Response<JokeDto>
}