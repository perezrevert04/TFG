package com.example.proyectonfc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.clases.RequirementsToLinkActivity

const val SPLASH_TIME: Long = 3000

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({

            val intent = Intent(this, RequirementsToLinkActivity::class.java)
            startActivity(intent)
            finish()

        }, SPLASH_TIME)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

}