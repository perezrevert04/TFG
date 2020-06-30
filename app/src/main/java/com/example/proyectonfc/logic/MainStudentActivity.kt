package com.example.proyectonfc.logic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.proyectonfc.R
import com.example.proyectonfc.StudentSignActivity
import kotlinx.android.synthetic.main.activity_main_student.*

class MainStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main_student)

        buttonSign.setOnClickListener { startActivity( Intent(this, StudentSignActivity::class.java) ) }
    }
}