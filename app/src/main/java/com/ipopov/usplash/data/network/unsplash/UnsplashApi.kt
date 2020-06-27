package com.ipopov.usplash.data.network.unsplash

import com.ipopov.usplash.data.network.unsplash.search.PhotoSearchResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("search/photos")
    fun search(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Deferred<Response<PhotoSearchResponse>>

}