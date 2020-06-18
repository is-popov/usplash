package com.ipopov.usplash.ui.base.permissions

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission

class PermissionManager {

    fun isLocationPermissionGranted(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
    }

    fun isCameraPermissionGranted(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(context, CAMERA) == PERMISSION_GRANTED &&

                checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED &&
                checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
    }

    fun isStoragePermissionGranted(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED &&
                checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
    }

    fun isContactsPermissionGranted(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(context, READ_CONTACTS) == PERMISSION_GRANTED
    }

    fun requestLocationPermissions(activity: Activity, permission: Permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), permission.code
            )
        }
    }

    fun requestCameraPermissions(activity: Activity, permission: Permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE), permission.code
            )
        }
    }

    fun requestStoragePermissions(activity: Activity, permission: Permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), permission.code
            )
        }
    }

    fun requestContactsPermissions(activity: Activity, permission: Permission){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(READ_CONTACTS), permission.code
            )
        }
    }

    fun isPermissionGranted(permission: Permission, results: IntArray): Boolean {
        return when (permission) {
            Permission.CAMERA -> results.size > 1 &&
                    results[0] == PERMISSION_GRANTED && results[1] == PERMISSION_GRANTED
            Permission.LOCATION -> results.isNotEmpty() && results[0] == PERMISSION_GRANTED
            Permission.STORAGE -> results.size > 1 &&
                    results[0] == PERMISSION_GRANTED && results[1] == PERMISSION_GRANTED
            Permission.CONTACTS -> results.isNotEmpty() &&
                    results[0] == PERMISSION_GRANTED
        }
    }

}
