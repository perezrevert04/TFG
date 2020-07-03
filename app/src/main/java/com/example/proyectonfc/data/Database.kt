package com.example.proyectonfc.data

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.proyectonfc.model.Person
import com.example.proyectonfc.model.Report
import com.example.proyectonfc.model.ReportFilter
import com.example.proyectonfc.model.Role

const val TABLE_LINKED_PERSON = "TABLE_LINKED_PERSON"
const val TABLE_REPORT = "TABLE_REPORT"

class Database(context: Context) : SQLiteOpenHelper(context, "shopping_notes", null, 1), DatabaseDAO {

    override fun onCreate(db: SQLiteDatabase) {
        createLinkedPersonTable(db)
        createReportTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }

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
        TODO("Not yet implemented")
    }

    override fun addReport(report: Report): Boolean {
        val values = ContentValues().apply {
            put("teacher", report.teacher)
            put("subject_code", report.subjectCode)
            put("subject_name", report.subjectName)
            put("class", report.group)
            put("classroom", report.classroom)
            put("date", report.date)
            put("hour", report.hour)
            put("duration", report.duration)
            put("attendance", report.attendance)
            put("comments", report.comments)
        }

        return writableDatabase.insert(TABLE_REPORT, null, values) > -1
    }

    override fun getReportById(id: String): Report {
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_REPORT WHERE id = $id", null)
        cursor.moveToFirst()

        val report = Report()
        report.id = cursor.getInt(0)
        report.teacher = cursor.getString(1)
        report.subjectCode = cursor.getString(2)
        report.subjectName = cursor.getString(3)
        report.group = cursor.getString(4)
        report.classroom = cursor.getString(5)
        report.date = cursor.getString(6)
        report.hour = cursor.getString(7)
        report.duration = cursor.getString(8)
        report.attendance = cursor.getInt(9)
        report.comments = cursor.getString(10)

        cursor.close()

        return report
    }

    override fun getAllReports(): ArrayList<Report> {
        val array: ArrayList<Report> = arrayListOf()

        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_REPORT", null)

        var report: Report
        while (cursor.moveToNext()) {
            report = Report()

            report.id = cursor.getInt(0)
            report.teacher = cursor.getString(1)
            report.subjectCode = cursor.getString(2)
            report.subjectName = cursor.getString(3)
            report.group = cursor.getString(4)
            report.classroom = cursor.getString(5)
            report.date = cursor.getString(6)
            report.hour = cursor.getString(7)
            report.duration = cursor.getString(8)
            report.attendance = cursor.getInt(9)
            report.comments = cursor.getString(10)

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

            report.id = cursor.getInt(0)
            report.teacher = cursor.getString(1)
            report.subjectCode = cursor.getString(2)
            report.subjectName = cursor.getString(3)
            report.group = cursor.getString(4)
            report.classroom = cursor.getString(5)
            report.date = cursor.getString(6)
            report.hour = cursor.getString(7)
            report.duration = cursor.getString(8)
            report.attendance = cursor.getInt(9)
            report.comments = cursor.getString(10)

            array.add(report)
        }

        cursor.close()

        return array
    }

    override fun removeReport(id: Int): Boolean {
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
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        "teacher TEXT , " +
                        "subject_code TEXT , " +
                        "subject_name TEXT , " +
                        "class TEXT , " +
                        "classroom TEXT ," +
                        "date TEXT , " +
                        "hour TEXT , " +
                        "duration TEXT ," +
                        "attendance INTEGER , " +
                        "comments TEXT " +
                        ")"
        )
    }

}