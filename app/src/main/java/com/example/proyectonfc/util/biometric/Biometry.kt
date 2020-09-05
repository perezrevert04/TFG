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

        val biometricPrompt = BiometricPrompt(
                context as FragmentActivity, // fragmentActivity
                ContextCompat.getMainExecutor(context), // executor
                callback(method) // authenticationCallback
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setConfirmationRequired(!facial)
                .setDeviceCredentialAllowed(true)
                .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun callback(method: () -> Unit) : BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                toast("Error de autenticación: $errString")
            }

            override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                method()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                toast("Autenticación fallida")
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}