package com.example.proyectonfc.logic.nearby

enum class NearbyCode(val code: Int, val msg: String) {
    UNKNOWN(0, "Se ha producido un error desconocido..."),
    UNREGISTERED(1, "No estás dado de alta en esta asignatura..."),
    DUPLICATED(2, "Ya tienes registrada la asistencia en esta asignatura..."),
    SUCCESS(3, "Fichaje realizado con éxito.");
}