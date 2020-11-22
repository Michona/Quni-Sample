package com.qusion.quni.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quni.apolloservice.api.NetworkResult
import com.qusion.quni.domain.Event
import com.qusion.quni.domain.repos.JokesRepository
import kotlinx.coroutines.launch

class JokeDetailViewModel(
    private val repository: JokesRepository
) : ViewModel() {

    private val _jokeContent = MutableLiveData<String>()
    val jokeContent: LiveData<String> = _jokeContent

    private val _isJokeLoading = MutableLiveData(false)
    val isJokeLoading: LiveData<Boolean> = _isJokeLoading

    private val _nextJokeError = MutableLiveData<Event<Boolean>>()
    val nextJokeError: LiveData<Event<Boolean>> = _nextJokeError

    init {
        fetchNextRandomJoke()
    }

    fun fetchNextRandomJoke() = viewModelScope.launch {
        _isJokeLoading.value = true
        when (val response = repository.getRemoteRandomJoke()) {
            is NetworkResult.Success -> {
                _jokeContent.value = response.value.joke?.joke ?: ""
            }
            is NetworkResult.Error -> {
                _nextJokeError.value = Event(true)
            }
        }
        _isJokeLoading.value = false
    }
}
