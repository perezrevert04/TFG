package com.example.proyectonfc

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.proyectonfc.clases.MainActivity
import com.example.proyectonfc.util.CardInfo
import kotlinx.android.synthetic.main.activity_link_biometric_prompt.*
import org.jetbrains.anko.toast
import java.util.concurrent.Executor

class LinkBiometricPromptActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_biometric_prompt)

        buttonLink.visibility = View.INVISIBLE

        getData()
        prepareBiometricPrompt()

        buttonLink.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }

    private fun getData() {
        val card = intent.getSerializableExtra(CardInfo.CARD_INFO) as? CardInfo

        textViewRole.text = card?.funcion
        textViewName.text = card?.nombre
        textViewDNI.text = card?.dni
        textViewCard.text = card?.tarjeta
        textViewValidity.text = card?.vigencia
        textViewStatus.text = card?.estado
    }

    private fun prepareBiometricPrompt() {
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        toast("Authentication error: $errString")
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        textViewAuthenticate.visibility = View.INVISIBLE
                        buttonAuthenticate.visibility = View.INVISIBLE
                        buttonLink.visibility = View.VISIBLE
                        toast("Authentication succeeded!")
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        toast("Authentication failed")
                    }
                })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Confirmación")
                .setSubtitle("Autentíquese para vincular")
                .setDeviceCredentialAllowed(true)
                .build()

        buttonAuthenticate.setOnClickListener { biometricPrompt.authenticate(promptInfo) }
    }

}