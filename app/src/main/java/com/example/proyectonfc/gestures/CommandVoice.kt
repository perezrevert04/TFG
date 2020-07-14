package com.example.proyectonfc.gestures

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import java.util.*

class CommandVoice(val context: Activity) {

    fun startCommandVoice() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dame una orden.")
        try {
            context.startActivityForResult(intent, 1234)
        } catch (ex: ActivityNotFoundException) {
            Log.e("AppLog", ex.message)
            ex.printStackTrace()
        }
    }

}