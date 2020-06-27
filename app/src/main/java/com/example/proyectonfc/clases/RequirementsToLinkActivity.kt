package com.example.proyectonfc.clases

import android.app.AlertDialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import com.example.proyectonfc.LinkCardActivity
import com.example.proyectonfc.R
import kotlinx.android.synthetic.main.activity_requirements_to_link.*

class RequirementsToLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requirements_to_link)

        if (BiometricManager.from(this).canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            textViewSecond.text = "2. Que el dispositivo esté protegido con algún tipo de bloqueo de pantalla."
        }

        buttonNext.setOnClickListener {
            if (!isNetworkAvailable()) {
                showAlert(Intent(Settings.ACTION_WIRELESS_SETTINGS), "Es necesario tener acceso a internet.")
            } else if (!(getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isDeviceSecure) {
                showAlert(Intent(Settings.ACTION_SECURITY_SETTINGS), "El dispositivo debe estar dispositivo debe estar protegido con algún tipo de bloqueo de pantalla.")
            } else {
                startActivity(Intent(this, LinkCardActivity::class.java))
            }
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showAlert(intent: Intent, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Aviso").setMessage(message)

        builder.setPositiveButton("Ajustes") { _, _ -> startActivity(intent) }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}