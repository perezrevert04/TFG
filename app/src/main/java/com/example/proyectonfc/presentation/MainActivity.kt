package com.example.proyectonfc.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.proyectonfc.R
import com.example.proyectonfc.presentation.teacher.consults.ShowReportsActivity
import com.example.proyectonfc.presentation.teacher.report.AsignaturasProfesor
import com.example.proyectonfc.presentation.teacher.management.subjects.Configuracion
import com.example.proyectonfc.presentation.student.MainStudentActivity
import kotlinx.android.synthetic.main.activity_main.*

const val WES_PERMISSION_REQUEST_CODE = 1
const val AFL_PERMISSION_REQUEST_CODE = 2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        if (SplashScreenActivity.notVisited) {
            val intent = Intent(this, SplashScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            val intent = Intent(this, MainStudentActivity::class.java)
            startActivity(intent)
            finish()
        }

//        TODO: Gestionar permisos de la aplicación
//        else {
//            handlePermissions()
//        }

        buttonStart.setOnClickListener { v: View ->
            val intent = Intent(v.context, AsignaturasProfesor::class.java)
            startActivity(intent)
        }

        buttonManageSubjects.setOnClickListener { v: View ->
            val intent = Intent(v.context, Configuracion::class.java)
            startActivity(intent)
        }

        buttonConsultParts.setOnClickListener { v: View ->
            val intent = Intent(v.context, ShowReportsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handlePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WES_PERMISSION_REQUEST_CODE)
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), AFL_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            false
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        super.onResume()

        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WES_PERMISSION_REQUEST_CODE -> if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showAlert("Permitir acceso", "Esta aplicación utiliza el sistema de almacenamiento del dispositivo para realizar algunas de sus funciones.")
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), AFL_PERMISSION_REQUEST_CODE)
            }
            AFL_PERMISSION_REQUEST_CODE -> if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showAlert("Permitir acceso", "Esta aplicación utiliza la ubicación para poder realizar el fichaje de smartphone a smartphone.")
            }
        }
    }

    private fun goToAppPermissions() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showAlert(title: String, subtitle: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(subtitle)
        builder.setCancelable(false)

        builder.setPositiveButton("Ok!") { _, _ -> goToAppPermissions() }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}