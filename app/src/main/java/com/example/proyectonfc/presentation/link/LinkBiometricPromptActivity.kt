package com.example.proyectonfc.presentation.link

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.model.Person
import com.example.proyectonfc.presentation.SplashScreenActivity
import com.example.proyectonfc.util.biometric.Biometry
import kotlinx.android.synthetic.main.activity_link_biometric_prompt.*
import org.jetbrains.anko.toast

class LinkBiometricPromptActivity : AppCompatActivity() {

    private val database by lazy { (application as Global).database }
    private lateinit var person: Person

    private lateinit var biometry: Biometry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_biometric_prompt)

        buttonLink.visibility = View.INVISIBLE

        biometry = Biometry(context = this, title = "Confirmación", subtitle = "Autentíquese para vincular")

        obtainData()

        buttonAuthenticate.setOnClickListener {
            biometry.authenticate {
                textViewAuthenticate.visibility = View.INVISIBLE
                buttonAuthenticate.visibility = View.INVISIBLE
                buttonLink.visibility = View.VISIBLE
                toast("Autenticación realizada con éxito")
            }
        }

        buttonLink.setOnClickListener {
            if (database.addLinkedPerson(person)) {
                startActivity(Intent(this, SplashScreenActivity::class.java))
                toast("Vinculado con éxito. ¡Bienvenido!")
                finish()
            }
            else {
                toast("Se ha producido un error: inténtalo de nuevo más tarde")
            }
        }
    }

    private fun obtainData() {
        person = intent.getSerializableExtra(Person.CARD_INFO) as Person

        textViewStudentIdentifier.text = person.identifier
        textViewRole.text = person.role.role
        textViewName.text = person.name
        textViewDNI.text = person.dni
        textViewCard.text = person.card
        textViewValidity.text = person.validity
        textViewStatus.text = person.status
    }

}