package com.quni.apolloservice

import com.quni.apolloservice.api.IApolloService
import com.quni.apolloservice.impl.ApolloServiceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val apolloModule = module {
    single<IApolloService> { ApolloServiceImpl(androidContext()) }
}
