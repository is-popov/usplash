package com.ipopov.usplash.ui.search

import com.ipopov.usplash.data.network.unsplash.search.Result

data class PhotoSearchState(

    val started: Boolean = false,

    val continued: Boolean = false,

    val items: List<Result>

)

