package com.ipopov.usplash.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.ipopov.usplash.ui.base.permissions.Permission
import com.ipopov.usplash.ui.base.permissions.PermissionCallback

abstract class BaseFragment<V : BaseViewModel> : Fragment(), BaseView<V> {

    protected lateinit var model: V

    val baseActivity get() = activity as BaseActivity<*>?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(this).get(modelClass)
        model.baseObservable.observe(this, Observer {
            val state = it ?: return@Observer
            when {
                state.loading -> showProgressBar()
                state.loaded -> hideProgressBar()
                state.showError -> showToast(messageId = state.errorId)
            }
        })
    }

    override fun showToast(@StringRes messageId: Int, duration: Int) {
        baseActivity?.showToast(messageId, duration)
    }

    override fun showToast(message: String, duration: Int) {
        baseActivity?.showToast(message, duration)
    }

    override fun showProgressBar() {
        baseActivity?.showProgressBar()
    }

    override fun hideProgressBar() {
        baseActivity?.hideProgressBar()
    }

    override fun showSnackBar(
        @StringRes messageId: Int,
        @Duration duration: Int,
        @StringRes actionTitle: Int?,
        action: () -> Unit,
        view: View?
    ) {
        baseActivity?.showSnackBar(messageId, duration, actionTitle, action, view)
    }

    override fun hideSnackBar() {
        baseActivity?.hideSnackBar()
    }

    override fun requestActivityResult(intent: Intent, callback: BaseActivity.ResultCallback) {
        baseActivity?.requestActivityResult(intent, callback)
    }

    override fun requestPermission(permission: Permission, callback: PermissionCallback) {
        baseActivity?.requestPermission(permission, callback)
    }

}