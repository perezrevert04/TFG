package com.example.proyectonfc.clases

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
import com.example.proyectonfc.ShowReportsActivity
import com.example.proyectonfc.SplashScreenActivity
import kotlinx.android.synthetic.main.activity_main.*

const val PERMISSION_REQUEST_CODE = 1000

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
//       // Si es un estudiante derivar a main student activity
//            val intent = Intent(this, MainStudentActivity::class.java)
//            startActivity(intent)
//            finish()
//

//        val intent = Intent(this, NearbyTestActivity::class.java)
//        startActivity(intent)
//        finish()


        else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }

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
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permitir acceso")
                builder.setMessage("Esta aplicación utiliza el sistema de almacenamiento del dispositivo para realizar algunas de sus funciones.")
                builder.setCancelable(false)

                builder.setPositiveButton("Ok!") { _, _ ->

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:$packageName"))
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()

                }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        }
    }
}