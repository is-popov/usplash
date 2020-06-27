package com.ipopov.usplash.data.network

import com.ipopov.usplash.data.network.unsplash.UnsplashApi

abstract class ApiProvider {

    abstract val unsplash: UnsplashApi

}