package com.example.proyectonfc.presentation.teacher.management.subjects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.model.Subject
import kotlinx.android.synthetic.main.activity_edit_subject.*

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
    }
}