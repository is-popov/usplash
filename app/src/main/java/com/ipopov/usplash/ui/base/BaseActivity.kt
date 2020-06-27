package com.ipopov.usplash.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import com.ipopov.usplash.ui.base.permissions.Permission
import com.ipopov.usplash.ui.base.permissions.PermissionCallback
import com.ipopov.usplash.ui.base.permissions.PermissionManager


abstract class BaseActivity<V : BaseViewModel> : AppCompatActivity(), BaseView<V> {

    protected lateinit var model: V

    protected abstract val viewRoot: View?

    protected var viewProgressBar: View? = null

    private var activityRequestMap: SparseArray<ResultCallback> = SparseArray()
    private var permissionCallbackMap: HashMap<Permission, PermissionCallback> = HashMap()
    private var permissionManager: PermissionManager = PermissionManager()
    private var currentRequestCode: Int = 0

    private var snackBar: Snackbar? = null

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
        Toast.makeText(this, messageId, duration).show()
    }

    override fun showToast(message: String, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }

    override fun showProgressBar() {
        viewProgressBar?.apply {
            if (!this.isShown) {
                this.visibility = View.VISIBLE
            }
        }
    }

    override fun hideProgressBar() {
        viewProgressBar?.apply {
            if (this.isShown) {
                this.visibility = View.GONE
            }
        }
    }

    override fun showSnackBar(
        @StringRes messageId: Int,
        @Duration duration: Int,
        @StringRes actionTitle: Int?,
        action: () -> Unit,
        view: View?
    ) {
        hideSnackBar()
        val contentView = view ?: viewRoot ?: return

        Snackbar.make(contentView, messageId, duration)
            .apply {
                snackBar = this

                if (actionTitle != null) {
                    setAction(actionTitle) { action.invoke() }
                }

                show()
            }
    }

    override fun hideSnackBar() {
        snackBar?.apply {
            if (isShown) {
                dismiss()
            }
        }
    }

    //region ActivityRequests

    interface ResultCallback {
        fun onResult(resultData: Intent?, resultCode: Int)
    }

    override fun requestActivityResult(intent: Intent, callback: ResultCallback) {
        activityRequestMap.put(currentRequestCode, callback)
        startActivityForResult(intent, currentRequestCode)
        currentRequestCode++
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityRequestMap[requestCode]?.onResult(data, resultCode)
        activityRequestMap.remove(requestCode)
    }

    //endregion

    //region RuntimePermissions

    override fun requestPermission(permission: Permission, callback: PermissionCallback) {
        permissionCallbackMap[permission] = callback
        if (isPermissionGranted(permission)) {
            callback.onGranted()
        } else {
            requestPermission(permission)
        }
    }

    private fun isPermissionGranted(permission: Permission): Boolean {
        return when (permission) {
            Permission.CAMERA -> permissionManager.isCameraPermissionGranted(this)
            Permission.LOCATION -> permissionManager.isLocationPermissionGranted(this)
            Permission.STORAGE -> permissionManager.isStoragePermissionGranted(this)
            Permission.CONTACTS -> permissionManager.isContactsPermissionGranted(this)
        }
    }

    private fun requestPermission(permission: Permission) {
        when (permission) {
            Permission.CAMERA -> permissionManager.requestCameraPermissions(this, permission)
            Permission.LOCATION -> permissionManager.requestLocationPermissions(this, permission)
            Permission.STORAGE -> permissionManager.requestStoragePermissions(this, permission)
            Permission.CONTACTS -> permissionManager.requestContactsPermissions(this, permission)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        val permission = Permission.codeOf(requestCode) ?: return
        if (permission in listOf(Permission.CAMERA, Permission.LOCATION, Permission.STORAGE)) {
            if (permissionManager.isPermissionGranted(permission, results)) {
                permissionCallbackMap[permission]?.onGranted()
            } else {
                permissionCallbackMap[permission]?.onDenied()
            }
        }
        permissionCallbackMap.remove(permission)
    }

    //endregion

}
