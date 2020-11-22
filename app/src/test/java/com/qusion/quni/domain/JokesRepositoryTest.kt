package com.qusion.quni.domain

import com.apollographql.apollo.api.Operation
import com.quni.apolloservice.JokeQuery
import com.quni.apolloservice.api.IApolloService
import com.quni.apolloservice.api.NetworkResult
import com.qusion.quni.domain.repos.JokesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class JokesRepositoryTest {

    @MockK
    private lateinit var apolloServiceMock: IApolloService

    /* Class under test. */
    private lateinit var jokesRepository: JokesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        jokesRepository = JokesRepository(apolloServiceMock)
    }

    @Test
    fun `random joke should call joke query`() {

        coEvery {
            apolloServiceMock.safeQuery<Operation.Data, Operation.Data, Operation.Variables>(any())
        } returns NetworkResult.Error()

        runBlocking {
            jokesRepository.getRemoteRandomJoke()
        }

        coVerify {
            apolloServiceMock.safeQuery<JokeQuery.Data, JokeQuery.Data, Operation.Variables>(any())
        }
    }
}