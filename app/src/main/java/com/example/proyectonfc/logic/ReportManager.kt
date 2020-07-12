package com.example.proyectonfc.logic

import android.content.Context
import com.example.proyectonfc.data.Database
import com.example.proyectonfc.data.StorageStudentsAttendance
import com.example.proyectonfc.model.Report
import com.example.proyectonfc.model.Student


class ReportManager(val context: Context, val database: Database) {

    fun saveReport(report: Report, students: ArrayList<Student>) {
        val storageXml = StorageStudentsAttendance(context, report.id)
        storageXml.saveStudentsList(students)

        database.addReport(report)
    }
}