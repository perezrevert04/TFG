package com.example.proyectonfc.presentation.teacher.management.consults

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.model.Person
import com.example.proyectonfc.model.Report
import kotlinx.android.synthetic.main.activity_report_data.*

class ReportDataActivity : AppCompatActivity() {

    companion object { const val EXTRA_REPORT = "EXTRA_REPORT" }

    private val person: Person by lazy { (application as Global).linked }
    private lateinit var report: Report

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_data)

        report = intent.getSerializableExtra(EXTRA_REPORT) as Report

        showInfoInScreen()
    }

    private fun showInfoInScreen() {
        val title = report.subject.code + ": " + report.subject.name
        textViewSubject.text = title
        val teacher = person.name + "(${person.dni})"
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

    private fun getExtras() {

//        val subject = report.subjectCode + ": " + report.subjectName
//        textViewSubject.text = subject
//        textViewTeacher.text = report.teacher
//        textViewGroup.text = report.group
//        textViewClassroom.text = report.classroom
//        textViewDate.text = report.date
//        textViewHour.text = report.hour
//        textViewDurationRepData.text = report.duration
//        textViewAttendance.text = report.attendance.toString()
//        textViewComments.text = report.comments
    }
}