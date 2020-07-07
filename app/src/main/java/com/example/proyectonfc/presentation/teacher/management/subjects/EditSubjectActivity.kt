package com.example.proyectonfc.presentation.teacher.management.subjects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.db.DataBase
import com.example.proyectonfc.model.Subject
import kotlinx.android.synthetic.main.activity_edit_subject.*
import org.jetbrains.anko.toast

class EditSubjectActivity : AppCompatActivity() {

    lateinit var subject: Subject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_subject)

        subject = intent.getSerializableExtra("ASIGNATURA") as Subject
        title = subject.code

        textViewSubjectCode.text = subject.code
        editTextDegree.setText(subject.degree)
        editTextSubjectName.setText(subject.name)
        editTextSchoolYear.setText(subject.schoolYear)
        editTextDepartment.setText(subject.department)
        editTextLanguage.setText(subject.language)
        editTextDuration.setText(subject.duration)

        buttonSaveSubjectData.setOnClickListener {
            val updated = Subject(
                    code = subject.code,
                    name = editTextSubjectName.text.toString(),
                    degree = editTextDegree.text.toString(),
                    schoolYear = editTextSchoolYear.text.toString(),
                    department = editTextDepartment.text.toString(),
                    language = editTextLanguage.text.toString(),
                    duration = editTextDuration.text.toString()
            )

            DataBase(applicationContext).updateSubject(updated)
            toast("¡Asignatura actualizada!")
            onBackPressed()
        }
    }
}