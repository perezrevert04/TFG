package com.example.proyectonfc.clases

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import kotlinx.android.synthetic.main.activity_add_comment.*
import org.jetbrains.anko.toast
import java.util.*

const val REQ_CODE = 100

class AddComment : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)

        valueComment.setText(intent.getStringExtra("comments"))
        Log.d("AppLog", "original comments received")

        fab.setOnClickListener {
            intent.putExtra("comments", valueComment.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        speak.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla")
            try {
                startActivityForResult(intent, REQ_CODE)
            } catch (a: ActivityNotFoundException) {
                toast("Lo sentimos, tu dispositivo no soporta esta versión")
            } }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK && null != data) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val comment = valueComment.text.toString() + " " + result[0].capitalize() + "."
                valueComment.setText(comment.trim())
            }
        }
    }
}
