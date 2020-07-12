package com.example.proyectonfc.logic

import android.content.Context
import com.example.proyectonfc.data.Database
import com.example.proyectonfc.data.StorageStudentsAttendance
import com.example.proyectonfc.model.Report
import com.example.proyectonfc.model.ReportFilter
import com.example.proyectonfc.model.Student

class ReportManager(val context: Context, val database: Database) {

    fun getAllReports(): ArrayList<Report> {
        return database.getAllReports()
    }

    fun filterReports(filter: ReportFilter): ArrayList<Report> {
        return database.filterReports(filter)
    }

    fun saveReport(report: Report, students: ArrayList<Student>) {
        val storageXml = StorageStudentsAttendance(context, report.id)
        storageXml.saveStudentsList(students)

        database.addReport(report)
    }

    fun getReportAttendance(id: String): List<Student> {
        val storageXml = StorageStudentsAttendance(context, id)
        return storageXml.studentsList
    }

    fun removeReport(id: String): Boolean {
        val storageXml = StorageStudentsAttendance(context, id)
        return storageXml.removeAttendance() && database.removeReport(id)

    }
}