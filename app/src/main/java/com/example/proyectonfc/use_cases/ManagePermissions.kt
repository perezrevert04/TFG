package com.example.proyectonfc.use_cases

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat

const val WES_PERMISSION_REQUEST_CODE = 1
const val AFL_PERMISSION_REQUEST_CODE = 2

const val ALERT_TITLE = "Permitir acceso"
const val AFL_ALERT_MSG = "Esta aplicación necesita acceder a la ubicación para poder realizar el fichaje de smartphone a smartphone."
const val WES_ALERT_MSG = "Esta aplicación necesita acceder al sistema de almacenamiento del dispositivo para realizar algunas de sus funciones."

class ManagePermissions( private val activity: Activity ) {

    fun handle() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WES_PERMISSION_REQUEST_CODE)
        } else if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), AFL_PERMISSION_REQUEST_CODE)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            WES_PERMISSION_REQUEST_CODE -> if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showAlert(ALERT_TITLE, WES_ALERT_MSG)
            } else if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), AFL_PERMISSION_REQUEST_CODE)
            }
            AFL_PERMISSION_REQUEST_CODE -> if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showAlert(ALERT_TITLE, AFL_ALERT_MSG)
            }
        }
    }

    private fun showAlert(title: String, msg: String) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setCancelable(false)

        builder.setPositiveButton("Ok!") { _, _ -> goToAppPermissions() }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }


    private fun goToAppPermissions() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${activity.packageName}"))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
    }
}