package com.ipopov.usplash.ui.base

import androidx.annotation.StringRes
import com.ipopov.usplash.R

data class BaseState(

    val loading: Boolean = false,

    val loaded: Boolean = false,

    val showMessage: Boolean = false,

    val showError: Boolean = false,

    @StringRes
    val messageId: Int? = null,

    @StringRes
    val errorId: Int = R.string.view_default_error

)
