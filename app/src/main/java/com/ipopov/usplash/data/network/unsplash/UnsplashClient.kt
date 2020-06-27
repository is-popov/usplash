package com.ipopov.usplash.data.network.unsplash

import com.ipopov.usplash.data.network.BaseClient
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class UnsplashClient(private val preferences: UnsplashPreferences) : BaseClient<UnsplashApi>() {

    override fun getClient(): UnsplashApi {

        val interceptors = arrayListOf(UnsplashInterceptor(preferences))

        return buildClient(baseUrl = preferences.baseUrl, interceptors = interceptors)
    }

    class UnsplashInterceptor(private val preferences: UnsplashPreferences) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val authorization = "Client-ID ${preferences.accessKey}"

            val request = original
                .newBuilder()
                .header("Authorization", authorization)
                .build()

            return chain.proceed(request)
        }
    }
}