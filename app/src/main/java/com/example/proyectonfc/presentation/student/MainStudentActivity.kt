package com.example.proyectonfc.presentation.student

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import kotlinx.android.synthetic.main.activity_main_student.*

class MainStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main_student)

        /* TODO: Gestionar aquí permisos de la aplicación */

        buttonSign.setOnClickListener { startActivity( Intent(this, StudentSignActivity::class.java) ) }
        buttonInfo.setOnClickListener { startActivity( Intent(this, ManageStudentDataActivity::class.java) ) }
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
}