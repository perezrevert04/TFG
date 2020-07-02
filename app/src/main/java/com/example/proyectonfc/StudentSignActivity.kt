package com.example.proyectonfc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.logic.MainStudentActivity
import com.example.proyectonfc.logic.biometric.Biometry
import com.example.proyectonfc.logic.nearby.Discover
import com.example.proyectonfc.logic.nearby.NearbyCode
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import kotlinx.android.synthetic.main.activity_student_sign.*
import org.jetbrains.anko.toast
import java.nio.charset.StandardCharsets

class StudentSignActivity : AppCompatActivity() {

    private lateinit var discover: Discover
    private lateinit var biometry: Biometry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign)

        biometry = Biometry(this, title = "Autenticación", subtitle = "Ficha con tu huella.")

        activeList.setOnItemClickListener { _: AdapterView<*>?, _: View, pos: Int, _: Long ->
            val keys = ArrayList<String>(discover.map.keys)
            biometry.authenticate { discover.sendPayload(keys[pos], "3967203186") }
        }
    }

    override fun onStart() {
        super.onStart()
        discover = Discover(this, android.os.Build.MODEL, applicationContext.packageName, payloadCallback)

        discover.addObserver {
            if (discover.map.isEmpty()) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.INVISIBLE

            val collection = it.values
            val array = ArrayList<String>(collection)
            activeList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, array)
        }

        discover.start()
    }

    override fun onStop() {
        super.onStop()
        discover.stop()
    }

    /*** INICIO NEARBY DISCOVER ***/

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            // This always gets the full data of the payload. Will be null if it's not a BYTES payload. You can check the payload type with payload.getType().
            val receivedBytes = payload.asBytes()
            val msg = String(receivedBytes!!, StandardCharsets.UTF_8)
            toast(msg)
            if (NearbyCode.SUCCESS.msg == msg) {
                val intent = Intent(applicationContext, MainStudentActivity::class.java)
                startActivity(intent)
                discover.stop()
                finish()
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, payloadTransferUpdate: PayloadTransferUpdate) {
            // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately after the call to onPayloadReceived().
        }
    }
    /*** FIN NEARBY DISCOVER ***/
}