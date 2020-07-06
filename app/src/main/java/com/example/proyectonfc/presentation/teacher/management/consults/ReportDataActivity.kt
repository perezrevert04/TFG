package com.example.proyectonfc.presentation.teacher.management.consults

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.model.Report
import kotlinx.android.synthetic.main.activity_creacion_parte.textViewClassroom
import kotlinx.android.synthetic.main.activity_creacion_parte.textViewGroup
import kotlinx.android.synthetic.main.activity_creacion_parte.textViewTeacher
import kotlinx.android.synthetic.main.activity_report_data.*

class ReportDataActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REPORT = "EXTRA_REPORT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_report_data)

        getExtras()

        buttonClose.setOnClickListener { finish() }
    }

    private fun getExtras() {
        val report = intent.getSerializableExtra(EXTRA_REPORT) as Report

        val subject = report.subjectCode + ": " + report.subjectName
        textViewSubject.text = subject
        textViewTeacher.text = report.teacher
        textViewGroup.text = report.group
        textViewClassroom.text = report.classroom
        textViewDate.text = report.date
        textViewHour.text = report.hour
        textViewDurationRepData.text = report.duration
        textViewAttendance.text = report.attendance.toString()
        textViewComments.text = report.comments
    }
}