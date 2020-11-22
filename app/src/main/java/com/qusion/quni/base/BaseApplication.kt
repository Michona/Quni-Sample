package com.qusion.quni.base

import android.app.Application
import com.quni.apolloservice.apolloModule
import com.qusion.quni.BuildConfig
import com.qusion.quni.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    appModule,
                    apolloModule
                )
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
