package com.example.proyectonfc.logic.biometric

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class Biometry(
        val context: Context,
        var title: String = "Autenticación biométrica",
        var subtitle: String = "Use su huella para identificarse."
) {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private fun prepareBiometry(method: () -> Unit) {
        executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                toast("Authentication error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                method()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                toast("Authentication failed")
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDeviceCredentialAllowed(true)
                .build()
    }

    fun authenticate(method: () -> Unit) {
        prepareBiometry { method() }
        biometricPrompt.authenticate(promptInfo)
    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}