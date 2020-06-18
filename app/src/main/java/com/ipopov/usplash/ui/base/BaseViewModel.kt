package com.ipopov.usplash.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private val baseController = MutableLiveData<BaseState>()
    val baseObservable: LiveData<BaseState> = baseController

    protected fun setLoadingState() {
        baseController.value = BaseState(loading = true)
    }

    protected fun setLoadedState() {
        baseController.value = BaseState(loaded = true)
    }

    protected fun setErrorState(error: Int) {
        baseController.value = BaseState(showError = true, errorId = error)
    }

    override fun onCleared() {
//        compositeObservable.dispose()
    }

}
