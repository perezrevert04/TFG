package com.example.proyectonfc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.proyectonfc.clases.MainActivity
import com.example.proyectonfc.logic.Person
import kotlinx.android.synthetic.main.activity_link_biometric_prompt.*
import org.jetbrains.anko.toast
import java.util.concurrent.Executor

class LinkBiometricPromptActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val database by lazy { (application as Global).database }
    private lateinit var person: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_biometric_prompt)

        buttonLink.visibility = View.INVISIBLE

        getData()
        prepareBiometricPrompt()

        buttonLink.setOnClickListener {
            Log.d("AppLog", person.toString())
            Log.d("AppLog", "Añadiendo a la base de datos...")
            if (database.addLinkedPerson(person)) {
                toast("Vinculado con éxito")
                startActivity(Intent(this, MainActivity::class.java))
            }
            else {
                toast("Se ha producido un error: inténtalo de nuevo más tarde")
            }
        }
    }

    private fun getData() {
        person = intent.getSerializableExtra(Person.CARD_INFO) as Person

        textViewRole.text = person.role
        textViewName.text = person.name
        textViewDNI.text = person.dni
        textViewCard.text = person.card
        textViewValidity.text = person.validity
        textViewStatus.text = person.status
    }

    private fun prepareBiometricPrompt() {
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        toast("Error en la autenticación: $errString")
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        textViewAuthenticate.visibility = View.INVISIBLE
                        buttonAuthenticate.visibility = View.INVISIBLE
                        buttonLink.visibility = View.VISIBLE
                        toast("Autenticación realizada con éxito")
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        toast("Autenticación fallida")
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