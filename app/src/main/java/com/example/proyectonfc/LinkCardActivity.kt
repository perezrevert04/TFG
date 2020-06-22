package com.example.proyectonfc

import android.app.AlertDialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import com.example.proyectonfc.logic.Person
import com.example.proyectonfc.util.CaptureActivityPortrait
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_link_card.*
import org.jetbrains.anko.toast
import org.jetbrains.anko.webView
import org.jsoup.Jsoup


const val BASE_URL = "https://www.upv.es/pls/oalu/sic_tui.inicio?p_tui="
const val END_URL = "&P_IDIOMA=c"

const val MEME_URL = "https://ih1.redbubble.net/image.805673899.5571/flat,128x128,075,t.u8.jpg"
const val QR_CODE_EXAMPLE = "Ojalá vivas tiempos interesantes"

class LinkCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_card)

        buttonScanQR.setOnClickListener { initScan() }

        imageViewQR.setOnClickListener { initScan() }

        if (!isNetworkAvailable()) {
            showAlert("Necesitas acceso a internet para continuar.")
            finish()
        } else if (BiometricManager.from(this).canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            showAlert("El dispositivo debe tener asociadas credenciales biométricas para continuar.")
        } else if (BiometricManager.from(this).canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            showAlert("Se ha producido un error en el hardware de huellas biométricas. Inténtelo de nuevo más tarde.")
        } else if (BiometricManager.from(this).canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            if (!(getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isDeviceSecure)  {
                showAlert("El dispositivo debe estar dispositivo debe estar protegido con algún tipo de bloqueo de pantalla para continuar.")
            }
        }
    }

    private fun initScan() {
        val intent = IntentIntegrator(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        intent.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        intent.setPrompt("Escaneando carnet UPV ...")
        intent.setCameraId(0)
        intent.setBeepEnabled(true)
        intent.captureActivity = CaptureActivityPortrait::class.java
        intent.setBarcodeImageEnabled(false)
        intent.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result.contents == null) {
            toast("Escaneo cancelado")
        } else {
            val strParts = result.contents.split("/".toRegex()).toTypedArray()
            val scanned = strParts[strParts.size - 1]

            if (scanned == QR_CODE_EXAMPLE) webView().loadUrl(MEME_URL)
            else getWebsiteInfo(BASE_URL + scanned + END_URL)
        }
    }

    private fun getWebsiteInfo(url: String) {
        toast("Cargando datos...")

        Thread( Runnable {
            val person = Person()

            try {
                val doc = Jsoup.connect(url).get()
                val links = doc.select(Person.CSS_QUERY)

                links.forEachIndexed { index, it ->
                    when (it.text()) {
                        Person.CARD_TAG -> person.card = links[index + 1].text()
                        Person.STATE_TAG -> person.status = links[index + 1].text()
                        Person.VALIDITY_TAG -> person.validity = links[index + 1].text()
                        Person.DNI_TAG -> person.dni = links[index + 1].text()
                        Person.NAME_TAG -> {
                            person.name = links[index + 1].text()
                            person.role = links[index + 2].text()
                        }
                    }
                }

                Log.d("AppLog", person.toString())

                val intent = Intent(this, LinkBiometricPromptActivity::class.java)
                intent.putExtra(Person.CARD_INFO, person)
                startActivity(intent)

            } catch (e: Exception) {
                textViewInfo.text = e.message
                Log.e("AppLog", "Error en getWebsiteInfo: " + e.message)
            }
        }).start()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Aviso").setMessage(message)

        builder.setPositiveButton("Entendido!") { _, _ -> finish() }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}