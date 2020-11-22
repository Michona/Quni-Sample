package com.qusion.quni

import com.qusion.quni.domain.repos.JokesRepository
import com.qusion.quni.ui.viewmodel.JokeDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    //Domain
    single { JokesRepository(get()) }

    //ViewModels
    viewModel { JokeDetailViewModel(get()) }
}