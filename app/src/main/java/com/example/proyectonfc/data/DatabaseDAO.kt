package com.example.proyectonfc.data

interface DatabaseDAO {

    fun getAllStudents() // Devuelve todos los alumnos
    fun getStudentById(id: String) // Devuelve el alumno con el identificador id
    fun getAllTeachers() // Devuelve todos los profesores
    fun getTeachertById(id: String) // Devuelve el alumno con el identificador id
}