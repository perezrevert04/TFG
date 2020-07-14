package com.example.proyectonfc.gestures

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.data.CommandVoiceActionsData
import org.jetbrains.anko.toast
import java.util.*

class CommandVoiceActivity : AppCompatActivity() {

    private val commandVoiceData = CommandVoiceActionsData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_command_voice)

        startCommandVoice()
    }

    private fun startCommandVoice() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dame una orden.")
        try {
            startActivityForResult(intent, 1234)
        } catch (ex: ActivityNotFoundException) {
            Log.e("AppLog", ex.message)
            ex.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == RESULT_OK && null != data) {
            val array = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val string = array[0]
            val cl = commandVoiceData.processData(string)

            if (cl != null) {
                val intent = Intent(this, cl)
                startActivity(intent)
            } else {
                toast("Lo siento, no te he entendido... \uD83D\uDE25")
            }
        }

        finish()
    }
}