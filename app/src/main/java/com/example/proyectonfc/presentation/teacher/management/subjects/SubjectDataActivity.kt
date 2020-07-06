package com.example.proyectonfc.presentation.teacher.management.subjects

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.db.DataBase
import kotlinx.android.synthetic.main.activity_subject_data.*

class SubjectDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_data)

        val code: String = intent.getStringExtra("ASIGNATURA")
        title = code
        getSubject(code)

        buttonEditSubject.setOnClickListener {
            val intent = Intent(it.context, EditarAsignatura::class.java)
            intent.putExtra("ASIGNATURA", code)
            startActivity(intent)
        }

        buttonSubjectStudents.setOnClickListener {
            val intent = Intent(it.context, Alumnos::class.java)
            intent.putExtra("ASIGNATURA", code)
            startActivity(intent)
        }

        buttonSubjectGroups.setOnClickListener {

            val intent = Intent(it.context, Grupos::class.java)
            intent.putExtra("ASIGNATURA", code)
            startActivity(intent)
        }
    }

    private fun getSubject(code: String) {
        val db: SQLiteDatabase = DataBase(applicationContext).readableDatabase

        val cursor = db.rawQuery("SELECT * FROM ASIGNATURA WHERE id='$code'", null)

        while (cursor.moveToNext()) {
            textViewSubjectCode.text = code
            textViewSubjectName.text = cursor.getString(1)
            textViewSubjectDegree.text = cursor.getString(2)
            textViewSchoolYear.text = cursor.getString(3)
            textViewDepartment.text = cursor.getString(4)
            textViewLanguage.text = cursor.getString(5)
            textViewDuration.text = cursor.getString(6)
        }
    }
}