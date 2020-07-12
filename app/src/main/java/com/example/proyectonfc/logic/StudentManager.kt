package com.example.proyectonfc.logic

import com.example.proyectonfc.model.Student
import com.example.proyectonfc.db.DataBase as OldDatabase

class StudentManager(val database: OldDatabase) {

    fun getAllStudents(code: String): Map<String, Student> {
        return database.getAllStudents(code)
    }
}