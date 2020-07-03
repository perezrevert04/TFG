package com.example.proyectonfc.logic

const val ST = "ALUMNE/A"
const val TE = "PROFESSOR/A"
enum class Role(val role: String) {

    STUDENT(ST),
    TEACHER(TE);

    companion object {

    fun getRole(role: String): Role {
        return if (role == ST) { STUDENT } else { TEACHER }
    }
    }
}