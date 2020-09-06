package com.qusion.quni.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qusion.quni.domain.Event
import com.qusion.quni.domain.NetworkResult
import com.qusion.quni.domain.repos.JokesRepository
import com.qusion.quni.domain.usecases.GetNextJoke
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class JokeDetailViewModel(
    private val repository: JokesRepository,
    private val getNextJoke: GetNextJoke
) : ViewModel() {

    private val _jokeContent = MutableLiveData<String>()
    val jokeContent: LiveData<String> = _jokeContent


    private val _isJokeLoading = MutableLiveData(false)
    val isJokeLoading: LiveData<Boolean> = _isJokeLoading

    private val _nextJokeError = MutableLiveData<Event<Boolean>>()
    val nextJokeError: LiveData<Event<Boolean>> = _nextJokeError

    init {
        viewModelScope.launch {
            repository.getCachedRandomJoke().collect { jokeResult ->
                Timber.d("Joke Received: $jokeResult")
                _jokeContent.value = jokeResult.joke
            }
        }

        fetchNextRandomJoke()
    }

    fun fetchNextRandomJoke() = viewModelScope.launch {
        _isJokeLoading.value = true
        when (getNextJoke()) {
            is NetworkResult.Success -> {
                //Was saved to DB
            }
            is NetworkResult.Error -> {
                _nextJokeError.value = Event(true)
            }
        }
        _isJokeLoading.value = false
    }
}