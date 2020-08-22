package com.example.proyectonfc.presentation.student

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.use_cases.ManagePermissions
import kotlinx.android.synthetic.main.activity_main_student.*

class MainStudentActivity : AppCompatActivity() {

    lateinit var permissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_student)

        permissions = ManagePermissions(this)

        buttonSign.setOnClickListener { startActivity( Intent(this, StudentSignActivity::class.java) ) }
        buttonInfo.setOnClickListener { startActivity( Intent(this, ManageStudentDataActivity::class.java) ) }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAffinity()
            true
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

    override fun onStart() {
        super.onStart()
        permissions.handle()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        this.permissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}