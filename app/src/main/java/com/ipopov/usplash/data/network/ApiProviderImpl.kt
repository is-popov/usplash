package com.ipopov.usplash.data.network

import com.ipopov.usplash.BuildConfig
import com.ipopov.usplash.data.network.unsplash.UnsplashApi
import com.ipopov.usplash.data.network.unsplash.UnsplashClient
import com.ipopov.usplash.data.network.unsplash.UnsplashPreferences


class ApiProviderImpl : ApiProvider() {

    private object Holder {
        val INSTANCE = ApiProviderImpl()
    }

    companion object {
        val instance: ApiProviderImpl by lazy { Holder.INSTANCE }
    }

    //todo use firebase remote config instead
    private val unsplashPreferences = UnsplashPreferences(
        baseUrl = BuildConfig.UNSPLASH_BASE_URL,
        accessKey = BuildConfig.UNSPLASH_ACCESS_KEY
    )

    private val unsplashClient = UnsplashClient(unsplashPreferences)

    override var unsplash: UnsplashApi = unsplashClient.getClient()

}