package com.ipopov.usplash.data.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


abstract class BaseClient<T>() {

    abstract fun getClient(): T

    protected inline fun <reified T> buildClient(
        baseUrl: String,
        connectTimeoutInMillis: Long = 10000L,
        readTimeoutInMillis: Long = 15000L,
        logging: Boolean = true,
        interceptors: List<Interceptor> = emptyList(),
        callAdapterFactory: CallAdapter.Factory = CoroutineCallAdapterFactory(),
        converterFactory: Converter.Factory = GsonConverterFactory.create(
            GsonBuilder()
                .setLenient()
                .create()
        )
    ): T {
        val builder = OkHttpClient.Builder()
            .connectTimeout(connectTimeoutInMillis, TimeUnit.MILLISECONDS)
            .readTimeout(readTimeoutInMillis, TimeUnit.MILLISECONDS)

        for (interceptor in interceptors) {
            builder.addInterceptor(interceptor)
        }

        if (logging) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            builder.addInterceptor(loggingInterceptor)
        }

        val httpClient = builder.build()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .baseUrl(baseUrl)
            .client(httpClient)
            .build()

        return retrofit.create<T>(T::class.java)
    }

}