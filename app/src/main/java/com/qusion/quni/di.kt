package com.qusion.quni

import com.qusion.quni.data.db.AppDatabase
import com.qusion.quni.data.remote.RetrofitProvider
import com.qusion.quni.data.remote.api.JokesApi
import com.qusion.quni.domain.repos.JokesRepository
import com.qusion.quni.domain.usecases.GetNextJoke
import com.qusion.quni.ui.viewmodel.JokeDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val diModule = module {

    //Remote
    single<JokesApi> { RetrofitProvider.getClient().create(JokesApi::class.java) }
    //Db
    single { AppDatabase.getInstance(androidContext()).jokesDao() }

    //Domain
    single { JokesRepository(get(), get()) }
    single { GetNextJoke(get(), get()) }


    //ViewModels
    viewModel { JokeDetailViewModel(get(), get()) }
}