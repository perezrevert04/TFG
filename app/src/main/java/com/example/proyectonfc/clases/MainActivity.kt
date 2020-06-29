package com.example.proyectonfc.clases

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.ShowReportsActivity
import com.example.proyectonfc.SplashScreenActivity
import kotlinx.android.synthetic.main.activity_main.*

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

//        val intent = Intent(this, NearbyTestActivity::class.java)
//        startActivity(intent)
//        finish()

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
}