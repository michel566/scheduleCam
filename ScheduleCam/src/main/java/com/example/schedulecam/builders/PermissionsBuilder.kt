package com.example.schedulecam.builders

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionsBuilder private constructor(
    val app: AppCompatActivity,
    val actionOnGrantedPermission: () -> Unit,
){

    data class Builder(
        var app: AppCompatActivity,
        var actionOnGrantedPermission: () -> Unit,
        ){

        fun contexts(app: AppCompatActivity) = apply { this.app = app }
        fun actionOnGrantedPermission(action: () -> Unit) = apply { this.actionOnGrantedPermission = action }

        fun build(): PermissionsBuilder {
            if (allPermissionsGranted()) {
                actionOnGrantedPermission.invoke()
            } else {
                requestPermissions()
            }
            return PermissionsBuilder(app, actionOnGrantedPermission)
        }

        private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(app.baseContext, it) ==
                    PackageManager.PERMISSION_GRANTED
        }

        private fun requestPermissions() {
            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
        }

        private val activityResultLauncher =
            app.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            )
            { permissions ->
                // Handle Permission granted/rejected
                var permissionGranted = true
                permissions.entries.forEach {
                    if (it.key in REQUIRED_PERMISSIONS && !it.value)
                        permissionGranted = false
                }
                if (!permissionGranted) {
                    Toast.makeText(
                        app.baseContext,
                        "Permission request denied",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    actionOnGrantedPermission.invoke()
                }
            }

    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}