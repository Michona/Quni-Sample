package com.qusion.quni.domain.usecases

import com.qusion.quni.data.db.daos.JokesDao
import com.qusion.quni.domain.NetworkResult
import com.qusion.quni.domain.entities.JokeDto
import com.qusion.quni.domain.repos.JokesRepository
import timber.log.Timber

/** Fetches a random joke from BE and saves it in the local database. */
class GetNextJoke(private val jokesRepository: JokesRepository, private val jokesCache: JokesDao) {

    suspend operator fun invoke(): NetworkResult<JokeDto> {
        val response = jokesRepository.getRemoteRandomJoke()

        when (response) {
            is NetworkResult.Success -> {
                jokesCache.insert(response.value)
            }
            is NetworkResult.Error -> {
                Timber.e("$response")
            }
        }

        return response
    }
}