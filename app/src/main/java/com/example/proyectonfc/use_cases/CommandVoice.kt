package com.example.proyectonfc.use_cases

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import java.util.*

object CommandVoice {

    const val COMMAND_VOICE_CODE = 20

    fun launchVoice(activity: Activity, text: String) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text)
        try {
            activity.startActivityForResult(intent, COMMAND_VOICE_CODE)
        } catch (ex: ActivityNotFoundException) {
            Log.e("AppLog", ex.message)
            ex.printStackTrace()
        }
    }
}