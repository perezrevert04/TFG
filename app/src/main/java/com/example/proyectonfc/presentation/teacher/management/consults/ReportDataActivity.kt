package com.example.proyectonfc.presentation.teacher.management.consults

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.logic.PdfManager
import com.example.proyectonfc.logic.ReportManager
import com.example.proyectonfc.model.Person
import com.example.proyectonfc.model.Report
import com.example.proyectonfc.model.Student
import com.example.proyectonfc.presentation.teacher.report.AttendanceActivity
import kotlinx.android.synthetic.main.activity_report_data.*
import org.jetbrains.anko.toast

class ReportDataActivity : AppCompatActivity() {

    companion object { const val EXTRA_REPORT = "EXTRA_REPORT" }

    private val person: Person by lazy { (application as Global).linked }
    private lateinit var report: Report
    private lateinit var manager: ReportManager
    private lateinit var attendanceList: ArrayList<Student>

    /* TODO: Gestionar comandos por voz */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_data)

        report = intent.getSerializableExtra(EXTRA_REPORT) as Report
        manager = ReportManager(this, (application as Global).database)

        attendanceList = manager.getReportAttendance(report.id) as ArrayList<Student>

        buttonGeneratePdf.setOnClickListener {
            val pdf = PdfManager(report, person)
            pdf.create(attendanceList)
            pdf.open(this)
        }

        buttonShare.setOnClickListener {
            toast("Cargando...")
            val pdf = PdfManager(report, person)
            pdf.create(attendanceList)
            pdf.share(this)
        }

        showInfoInScreen()
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

    private fun showInfoInScreen() {
        val title = report.subject.code + ": " + report.subject.name
        val teacher = person.name + " (${person.dni})"

        textViewSubject.text = title
        textViewTeacher.text = teacher
        textViewGroup.text = report.group
        textViewClassroom.text = report.classroom
        textViewDate.text = report.date
        textViewHour.text = report.hour
        textViewDuration.text = report.subject.duration
        textViewCount.text = report.attendance.toString()

        if (report.comments.isNotEmpty()) {
            textViewEmptyComments.visibility = View.INVISIBLE
            textViewComments.text = report.comments
        }
    }
}