package com.ipopov.usplash.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ipopov.usplash.data.network.ApiProviderImpl
import com.ipopov.usplash.data.network.CallResult
import com.ipopov.usplash.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class PhotoSearchViewModel : BaseViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val apiProvider = ApiProviderImpl.instance

    private val searchController = MutableLiveData<PhotoSearchState>()
    val searchObserver: LiveData<PhotoSearchState> = searchController

    private val loadingController = MutableLiveData<Boolean>()
    val loadingObserver: LiveData<Boolean> = loadingController

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    val prefetch = 10

    private lateinit var query: String

    private var page: Int = 1
    private val perPage = 10

    fun onStartSearch(query: String) {
        this.query = query

        fetchPhotos(query = query, started = true)
    }

    fun onContinueSearch() {
        fetchPhotos(query = query, continued = true)
    }

    private fun fetchPhotos(query: String, started: Boolean = false, continued: Boolean = false) {
        this.isLoading = true

        loadingController.value = true

        scope.launch {
            val response = apiCall(
                call = {
                    apiProvider.unsplash.search(
                        query = query,
                        page = page,
                        perPage = perPage
                    ).await()
                }
            )

            val total = response?.total ?: 0

            isLastPage = total < page * perPage
            page++

            isLoading = false

            loadingController.postValue(false)
            searchController.postValue(
                PhotoSearchState(
                    started = started,
                    continued = continued,
                    items = response?.results ?: emptyList()
                )
            )
        }
    }

    private suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): T? {
        val result: CallResult<T> = safeApiResult(call)
        var data: T? = null

        when (result) {
            is CallResult.Success -> data = result.data
            is CallResult.Error -> {
            }
        }

        return data
    }

    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): CallResult<T> {
        val response = call.invoke()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            return CallResult.Success(body)
        }
        return CallResult.Error(IOException("Error occurred during getting safe Api result"))
    }

}
