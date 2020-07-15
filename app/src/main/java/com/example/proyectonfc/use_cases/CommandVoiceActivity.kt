package com.example.proyectonfc.use_cases

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.data.CommandVoiceActionsData
import org.jetbrains.anko.toast

class CommandVoiceActivity : AppCompatActivity() {

    private val commandVoiceData = CommandVoiceActionsData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_command_voice)

        CommandVoice.launchVoice(this, "Dame una orden.")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommandVoice.COMMAND_VOICE_CODE && resultCode == RESULT_OK && null != data) {
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