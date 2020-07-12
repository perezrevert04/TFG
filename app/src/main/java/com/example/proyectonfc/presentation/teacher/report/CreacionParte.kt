package com.example.proyectonfc.presentation.teacher.report

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.clases.AddComment
import com.example.proyectonfc.db.DataBase
import com.example.proyectonfc.logic.PdfManager
import com.example.proyectonfc.logic.ReportManager
import com.example.proyectonfc.logic.StudentManager
import com.example.proyectonfc.model.Person
import com.example.proyectonfc.model.Report
import com.example.proyectonfc.model.Student
import com.example.proyectonfc.util.biometric.Biometry
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import harmony.java.awt.Color
import kotlinx.android.synthetic.main.activity_creacion_parte.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream

class CreacionParte : AppCompatActivity() {

    companion object { const val REQ_CODE = 1213 }

    private val database by lazy { (application as Global).database }

    private lateinit var biometry: Biometry

    private lateinit var listaIdentificadores: MutableList<String>
    private lateinit var report: Report
    private lateinit var person: Person

    private lateinit var allStudents: Map<String, Student>
    private lateinit var attendanceList: ArrayList<Student>

    private lateinit var manager: ReportManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creacion_parte)

        biometry = Biometry(this, title = "Autenticación", subtitle = "Identifíquese para generar el pdf.")
        manager = ReportManager(this, database)
        getData()

        buttonAddComment.setOnClickListener {
            val intent = Intent(this, AddComment::class.java)
            intent.putExtra("comments", report.comments)
            startActivityForResult(intent, 1234)
        }

        buttonCrearPdf.setOnClickListener {
            biometry.authenticate  { finalizarParte() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_attendance, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_attendance -> {
                val intent = Intent(this, AttendanceActivity::class.java)
                intent.putExtra(AttendanceActivity.ATTENDANCE_LIST, attendanceList)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            report.comments = data?.extras?.getString("comments") ?: ""
            textViewComments.text = report.comments
            textViewEmptyComments.visibility = if (report.comments.isEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun getData() {

        report = intent.getSerializableExtra("ReportObject") as Report
        person = database.getLinkedPerson()

        val studentManager = StudentManager((application as Global).oldDatabase)
        allStudents = studentManager.getAllStudents(report.subject.code)
        listaIdentificadores = intent.getStringArrayListExtra("listaIdentificadores")

        attendanceList = arrayListOf()
        listaIdentificadores.forEach {
            val student: Student? = allStudents[report.subject.code + it]
            if (student != null) attendanceList.add(student)
        }

        val subject = "${report.subject.code}: ${report.subject.name}"
        val teacher = "${person.name} (${person.dni})"

        textViewFecha.text = report.date
        textViewCount.text = report.attendance.toString()
        textViewSubject.text = subject
        textViewTeacher.text = teacher
        textViewGroup.text = report.group
        textViewClassroom.text = report.classroom
        textViewHour.text = report.hour
        textViewDuration.text = report.subject.duration

    }

    private fun finalizarParte() {
        saveReport()

        try {
            val filename = report.getPdfName()

            val pdfManager = PdfManager(report, person)
            pdfManager.create(attendanceList)

            val retIntent = Intent()
            retIntent.putExtra("filename", filename)
            setResult(Activity.RESULT_OK, retIntent)
            finish()

        } catch (e: Exception) {
            toast("No se ha podido crear el archivo pdf")
        }
    }

    private fun saveReport() {
        manager.saveReport(report, attendanceList)
    }

}