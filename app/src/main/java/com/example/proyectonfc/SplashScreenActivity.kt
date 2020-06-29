package com.example.proyectonfc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.clases.MainActivity
import com.example.proyectonfc.clases.RequirementsToLinkActivity

const val DELAY_MILLIS: Long = 2000

class SplashScreenActivity : AppCompatActivity() {

    companion object {
        var notVisited = true // Para asegurar que sólo se carga una vez el splash
    }

    private val database by lazy { (application as Global).database }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)

        notVisited = false

        Handler().postDelayed({

            val cl = if (!database.deviceIsLinked()) {
                RequirementsToLinkActivity::class.java
            } else {
                MainActivity::class.java
            }

            val intent = Intent(this, cl)
            startActivity(intent)
            finish()

        }, DELAY_MILLIS)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

}