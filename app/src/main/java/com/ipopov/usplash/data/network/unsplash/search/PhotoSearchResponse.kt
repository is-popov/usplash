package com.ipopov.usplash.data.network.unsplash.search

import com.google.gson.annotations.SerializedName

class PhotoSearchResponse {

    @SerializedName("total")
    var total: Int? = null

    @SerializedName("total_pages")
    var totalPages: Int? = null

    @SerializedName("results")
    var results: List<Result>? = null

}

class Result {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("promoted_at")
    var promotedAt: Any? = null

    @SerializedName("width")
    var width: Int? = null

    @SerializedName("height")
    var height: Int? = null

    @SerializedName("color")
    var color: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("alt_description")
    var altDescription: String? = null

    @SerializedName("urls")
    var urls: Urls? = null

    @SerializedName("likes")
    var likes: Int? = null

}

class Urls {

    @SerializedName("raw")
    var raw: String? = null

    @SerializedName("full")
    var full: String? = null

    @SerializedName("regular")
    var regular: String? = null

    @SerializedName("small")
    var small: String? = null

    @SerializedName("thumb")
    var thumb: String? = null

}