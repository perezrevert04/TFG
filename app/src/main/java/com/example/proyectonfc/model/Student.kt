package com.example.proyectonfc.model

import java.io.Serializable

class Student(var id: String, var dni: String, var name: String) : Serializable {
    constructor() : this("", "", "")

    override fun toString(): String {
        return "$name ($dni)"
    }
}