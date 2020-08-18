package com.example.proyectonfc.util.biometric

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class Biometry(
        val context: Context,
        var title: String = "Autenticación biométrica",
        var subtitle: String = "Use su huella para identificarse."
) {

    fun authenticate(facial: Boolean = false, method: () -> Unit) {

        val executor = ContextCompat.getMainExecutor(context)

        val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
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

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setConfirmationRequired(!facial)
                .setDeviceCredentialAllowed(true)
                .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}