package com.example.proyectonfc.presentation.teacher.management.subjects

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.db.DataBase
import com.example.proyectonfc.model.Subject
import com.example.proyectonfc.use_cases.CommandVoiceActivity
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_subject_data.*

class SubjectDataActivity : AppCompatActivity() {

    lateinit var subject: Subject
    lateinit var code: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_data)
        Slidr.attach(this)

        code = intent.getStringExtra("ASIGNATURA")
        title = code

        buttonEditSubject.setOnClickListener {
            val intent = Intent(it.context, EditSubjectActivity::class.java)
            intent.putExtra("ASIGNATURA", subject)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_voice, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.command_voice -> {
            val intent = Intent(this, CommandVoiceActivity::class.java)
            this.startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        getSubject(code)
    }

    private fun getSubject(code: String) {
        val db: SQLiteDatabase = DataBase(applicationContext).readableDatabase

        val cursor = db.rawQuery("SELECT * FROM ASIGNATURA WHERE id='$code'", null)

        while (cursor.moveToNext()) {
            subject = Subject(
                    code = code,
                    name = cursor.getString(1),
                    degree = cursor.getString(2),
                    schoolYear = cursor.getString(3),
                    department = cursor.getString(4),
                    language = cursor.getString(5),
                    duration = cursor.getString(6)
            )

            textViewSubjectCode.text = code
            textViewSubjectName.text = subject.name
            textViewSubjectDegree.text = subject.degree
            textViewSchoolYear.text = subject.schoolYear
            textViewDepartment.text = subject.department
            textViewLanguage.text = subject.language
            textViewDuration.text = subject.duration
        }
    }
}