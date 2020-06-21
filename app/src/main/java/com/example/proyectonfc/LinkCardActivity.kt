package com.example.proyectonfc

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.util.CaptureActivityPortrait
import com.example.proyectonfc.util.CardInfo
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_link_card.*
import org.jetbrains.anko.toast
import org.jsoup.Jsoup

const val BASE_URL = "https://www.upv.es/pls/oalu/sic_tui.inicio?p_tui="

class LinkCardActivity : AppCompatActivity() {

    private val tvResult: TextView? = null
    private val ci: CardInfo? = CardInfo()
    private var dni: String? = null
    private var nombre: String? = null
    private var funcion: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_card)

        buttonScanQR.setOnClickListener { initScan() }
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
            toast("Cancelaste el escaneo")
        } else {
            val strParts = result.contents.split("/".toRegex()).toTypedArray()
            getWebsiteInfo(BASE_URL + strParts[strParts.size - 1])
        }
    }

    private fun getWebsiteInfo(url: String) {
        Thread( Runnable {
            val builder = StringBuilder()

            try {
                Log.d("AppLog", "Entrando en el try - catch...")
                val doc = Jsoup.connect(url).get()
                Log.d("AppLog", "doc: $doc")
                val title = doc.title()
                Log.d("AppLog", "title: $title")
                val links = doc.select(CardInfo.css_query)
                Log.d("AppLog", "links size: ${links.size}")

                Log.d("AppLog", "Entrando en el bucle...")

                links.forEach {
                    Log.d("AppLog", "Foreach link: $it")
                }

                Log.d("AppLog", "Saliendo del bucle...")


                ///for (Element link : links) {
                //More patterns at https://try.jsoup.org/~LGB7rk_atM2roavV0d-czMt3J_g
                //Selector info at https://jsoup.org/apidocs/index.html?org/jsoup/select/Selector.html

                //builder.append(title).append("\n");


                ///for (Element link : links) {

//                var i = 0
//                while (i < links.size) {
//                    val e: Element = links.get(i)
//                    var token: String? = null
//                    if (e != null) token = e.text()
//                    if (token != null && e!!.text().length > 0) {
//                        if (ci!!.isDNI(token)) {
//                            ci!!.setDNI(links.get(++i).text())
//                            dni = links.get(i).text()
//                        } else if (ci!!.isEstado(token)) ci!!.setEstado(links.get(++i).text()) else if (ci!!.isTarjeta(token)) ci!!.setTarjeta(links.get(++i).text()) else if (ci!!.isVigencia(token)) ci!!.setVigencia(links.get(++i).text()) else if (ci!!.isNombre(token)) {
//                            ci!!.setNombre(links.get(++i).text())
//                            nombre = links.get(i).text()
//                            ci!!.funcion = links.get(++i).text()
//                            funcion = links.get(i).text()
//                        }
//                    }
//                    i++
//                }

            } catch (e: Exception) {
                Log.e("AppLog", "Error en getWebsiteInfo: " + e.message)
            }
        }).start()
    }

}