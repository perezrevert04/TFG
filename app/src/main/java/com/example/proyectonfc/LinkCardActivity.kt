package com.example.proyectonfc

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.util.CaptureActivityPortrait
import com.example.proyectonfc.util.CardInfo
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_link_card.*
import org.jetbrains.anko.toast
import org.jetbrains.anko.webView
import org.jsoup.Jsoup


const val BASE_URL = "https://www.upv.es/pls/oalu/sic_tui.inicio?p_tui="
const val MEME_URL = "https://ih1.redbubble.net/image.805673899.5571/flat,128x128,075,t.u8.jpg"
const val QR_CODE_EXAMPLE = "Ojalá vivas tiempos interesantes"

class LinkCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_card)

        buttonScanQR.setOnClickListener { initScan() }
        if (!isNetworkAvailable()) {
            toast("Necesitas acceso a internet para este proceso")
            finish()
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
            else getWebsiteInfo(BASE_URL + scanned)
        }
    }

    private fun getWebsiteInfo(url: String) {
        Thread( Runnable {
            val card = CardInfo()

            try {
                val doc = Jsoup.connect(url).get()
                val links = doc.select(CardInfo.css_query)

                links.forEachIndexed { index, it ->
                    when (it.text()) {
                        CardInfo.CARD_TAG -> card.setTarjeta(it.nextElementSibling().text())
                        CardInfo.STATE_TAG -> card.setEstado(it.nextElementSibling().text())
                        CardInfo.VALIDITY_TAG -> card.setVigencia(it.nextElementSibling().text())
                        CardInfo.DNI_TAG -> card.setDNI(it.nextElementSibling().text());
                        CardInfo.NAME_TAG -> {
                            card.nombre = it.nextElementSibling().text()
                            card.funcion = links[index + 2].text()
                        }
                    }
                }

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

}