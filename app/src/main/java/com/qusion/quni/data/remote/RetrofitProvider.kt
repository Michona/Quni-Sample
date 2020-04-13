package com.qusion.quni.data.remote

import com.google.gson.GsonBuilder
import com.qusion.quni.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitProvider {

    companion object {
        @Volatile
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            return retrofit ?: synchronized(this) {
                retrofit ?: build().also {
                    retrofit = it
                }
            }
        }

        private fun build(): Retrofit {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    var request = chain.request()

                    val builder = request.newBuilder()
                    builder.header("Accept", ACCEPT_HEADER)
                    builder.header("User-Agent", USER_AGENT_HEADER)

                    request = builder.build()

                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }


        private const val ACCEPT_HEADER = "application/json"
        private const val USER_AGENT_HEADER = "Quni Sample App (https://github.com/Michona/Quni-Sample)"
    }

}