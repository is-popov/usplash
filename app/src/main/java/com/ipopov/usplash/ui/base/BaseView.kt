package com.ipopov.usplash.ui.base

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.ipopov.usplash.ui.base.permissions.Permission
import com.ipopov.usplash.ui.base.permissions.PermissionCallback

interface BaseView<V : BaseViewModel> {

    val modelClass: Class<V>

    fun showToast(messageId: Int, duration: Int = Toast.LENGTH_LONG)

    fun showToast(message: String, duration: Int = Toast.LENGTH_LONG)

    fun showProgressBar()

    fun hideProgressBar()

    fun showSnackBar(
        messageId: Int,
        duration: Int = Snackbar.LENGTH_LONG,
        actionTitle: Int? = null,
        action: () -> Unit = {},
        view: View? = null
    )

    fun hideSnackBar()

    fun requestActivityResult(intent: Intent, callback: BaseActivity.ResultCallback)

    fun requestPermission(permission: Permission, callback: PermissionCallback)

}