package dev.daeyeon.gaasproject.module

import dev.daeyeon.gaasproject.BuildConfig
import dev.daeyeon.gaasproject.data.remote.api.UpbitApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    val instance: UpbitApi by lazy { createUpbitApi() }

    fun createUpbitApi(): UpbitApi {
        return Retrofit.Builder()
            .baseUrl("https://api.upbit.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                        else HttpLoggingInterceptor.Level.NONE
                    })
                    .build()
            )
            .build()
            .create(UpbitApi::class.java)
    }
}