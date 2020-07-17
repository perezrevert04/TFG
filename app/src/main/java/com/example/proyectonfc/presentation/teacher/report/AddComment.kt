package com.example.proyectonfc.presentation.teacher.report

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.use_cases.CommandVoice
import kotlinx.android.synthetic.main.activity_add_comment.*
import org.jetbrains.anko.toast

class AddComment : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)

        valueComment.setText(intent.getStringExtra("comments"))

        fab.setOnClickListener {
            intent.putExtra("comments", valueComment.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()

            toast("Observación modificada")
        }

        speak.setOnClickListener {
            CommandVoice.launchVoice(this, "Habla para añadir observación")
        }

        if (intent.getBooleanExtra("openVoice", false)) speak.performClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommandVoice.COMMAND_VOICE_CODE && resultCode == RESULT_OK && null != data) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val comment = valueComment.text.toString() + " " + result[0].capitalize() + "."
            valueComment.setText(comment.trim())
        }
    }
}
