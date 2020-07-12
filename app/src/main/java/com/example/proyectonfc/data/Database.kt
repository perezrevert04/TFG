package com.example.proyectonfc.data

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.proyectonfc.model.*

const val DATABASE_NAME = "DATABASE_UPV.db"
const val DATABASE_VERSION = 1
const val TABLE_LINKED_PERSON = "TABLE_LINKED_PERSON"
const val TABLE_REPORT = "TABLE_REPORT"

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), DatabaseDAO {

    override fun onCreate(db: SQLiteDatabase) {
        createLinkedPersonTable(db)
        createReportTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { }

    override fun addLinkedPerson(person: Person): Boolean {
        if (deviceIsLinked()) return false;

        val values = ContentValues().apply {
            put("id", person.identifier)
            put("name", person.name)
            put("dni", person.dni)
            put("card", person.card)
            put("validity", person.validity)
            put("role", person.role.role)
            put("status", person.status)
        }

        return writableDatabase.insert(TABLE_LINKED_PERSON, null, values) > -1
    }

    override fun deviceIsLinked(): Boolean {
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_LINKED_PERSON", null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }

    override fun getLinkedPerson(): Person {
        if (!deviceIsLinked()) throw SQLException("No hay tarjeta vinculada a este dispositivo")

        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_LINKED_PERSON", null)
        cursor.moveToFirst()

        val person = Person()
        person.identifier = cursor.getString(0)
        person.name = cursor.getString(1)
        person.dni = cursor.getString(2)
        person.card = cursor.getString(3)
        person.validity = cursor.getString(4)
        person.role = Role.getRole(cursor.getString(5))
        person.status = cursor.getString(6)

        cursor.close()

        return person
    }

    override fun removeLinkedPerson(): Boolean {
        return writableDatabase.delete(TABLE_LINKED_PERSON, null, null) > 0
    }

    override fun addReport(report: Report): Boolean {
        val values = ContentValues().apply {
            put("id", report.id)
            put("date", report.date)
            put("comments", report.comments)
            put("attendance", report.attendance)
            put("subject_code", report.subject.code)
            put("subject_name", report.subject.name)
            put("subject_degree", report.subject.degree)
            put("subject_school_year", report.subject.schoolYear)
            put("subject_department", report.subject.department)
            put("subject_language", report.subject.language)
            put("subject_duration", report.subject.duration)
            put("classroom", report.classroom)
            put("group_name", report.group)
            put("hour", report.hour)
        }

        return writableDatabase.insert(TABLE_REPORT, null, values) > 0
    }

    override fun getReportById(id: String): Report {
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_REPORT WHERE id = $id", null)
        cursor.moveToFirst()

        val report = Report()

        report.id = cursor.getString(0)
        report.date = cursor.getString(1)
        report.comments = cursor.getString(2)
        report.attendance = cursor.getInt(3)
        report.subject.code = cursor.getString(4)
        report.subject.name = cursor.getString(5)
        report.subject.degree = cursor.getString(6)
        report.subject.schoolYear = cursor.getString(7)
        report.subject.department = cursor.getString(8)
        report.subject.language = cursor.getString(9)
        report.subject.duration = cursor.getString(10)
        report.classroom = cursor.getString(11)
        report.group = cursor.getString(12)
        report.hour = cursor.getString(13)

        cursor.close()

        return report
    }

    override fun getAllReports(): ArrayList<Report> {
        val array: ArrayList<Report> = arrayListOf()

        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_REPORT", null)

        var report: Report
        while (cursor.moveToNext()) {
            report = Report()

            report.id = cursor.getString(0)
            report.date = cursor.getString(1)
            report.comments = cursor.getString(2)
            report.attendance = cursor.getInt(3)
            report.subject.code = cursor.getString(4)
            report.subject.name = cursor.getString(5)
            report.subject.degree = cursor.getString(6)
            report.subject.schoolYear = cursor.getString(7)
            report.subject.department = cursor.getString(8)
            report.subject.language = cursor.getString(9)
            report.subject.duration = cursor.getString(10)
            report.classroom = cursor.getString(11)
            report.group = cursor.getString(12)
            report.hour = cursor.getString(13)

            array.add(report)
        }

        cursor.close()

        return array
    }

    override fun filterReports(filter: ReportFilter): ArrayList<Report> {
        val array: ArrayList<Report> = arrayListOf()

        val sql = "SELECT * FROM $TABLE_REPORT " +
                "WHERE (subject_code LiKE '%${filter.subject}%' OR " +
                "subject_name LiKE '%${filter.subject}%') AND " +
                "class LiKE '%${filter.group}%' AND " +
                "date LIKE '%${filter.date}%'"

        val cursor = readableDatabase.rawQuery(sql, null)

        var report: Report
        while (cursor.moveToNext()) {
            report = Report()

            report.id = cursor.getString(0)
            report.date = cursor.getString(1)
            report.comments = cursor.getString(2)
            report.attendance = cursor.getInt(3)
            report.subject.code = cursor.getString(4)
            report.subject.name = cursor.getString(5)
            report.subject.degree = cursor.getString(6)
            report.subject.schoolYear = cursor.getString(7)
            report.subject.department = cursor.getString(8)
            report.subject.language = cursor.getString(9)
            report.subject.duration = cursor.getString(10)
            report.classroom = cursor.getString(11)
            report.group = cursor.getString(12)
            report.hour = cursor.getString(13)

            array.add(report)
        }

        cursor.close()

        return array
    }

    override fun removeReport(id: String): Boolean {
        return writableDatabase.delete(TABLE_REPORT, "_id = $id", null) > 0
    }

    private fun createLinkedPersonTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE $TABLE_LINKED_PERSON (" +
                        "id INTEGER PRIMARY KEY , " +
                        "name TEXT , " +
                        "dni TEXT , " +
                        "card TEXT , " +
                        "validity TEXT , " +
                        "role TEXT , " +
                        "status TEXT " +
                        ")"
        )
    }

    private fun createReportTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE $TABLE_REPORT (" +
                        "id TEXT PRIMARY KEY , " +
                        "date TEXT , " +
                        "comments TEXT , " +
                        "attendance INTEGER , " +
                        "subject_code TEXT , " +
                        "subject_name TEXT ," +
                        "subject_degree TEXT , " +
                        "subject_school_year TEXT , " +
                        "subject_department TEXT , " +
                        "subject_language TEXT , " +
                        "subject_duration TEXT , " +
                        "classroom TEXT , " +
                        "group_name TEXT ," +
                        "hour TEXT " +
                        ")"
        )
    }

}