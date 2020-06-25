package com.example.proyectonfc.clases

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.SplashScreenActivity
import com.example.proyectonfc.db.DataBase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var db: DataBase
    private val database by lazy { (application as Global).database }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        db = DataBase(applicationContext, "DB6.db", null, 1)

        if (!database.deviceIsLinked()) {
            val intent = Intent(this, SplashScreenActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonStart.setOnClickListener { v: View ->
            val intent = Intent(v.context, AsignaturasProfesor::class.java)
            startActivityForResult(intent, 0)
        }

        buttonManageSubjects.setOnClickListener { v: View ->
            val intent = Intent(v.context, Configuracion::class.java)
            startActivityForResult(intent, 0)
        }

        buttonConsultParts.setOnClickListener { v: View ->
            val intent = Intent(v.context, DatosXml::class.java)
            startActivityForResult(intent, 0)
        }
    }
}