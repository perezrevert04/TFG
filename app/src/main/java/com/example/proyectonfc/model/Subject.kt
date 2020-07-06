package com.example.proyectonfc.model

import java.io.Serializable

class Subject(
        var code: String = "",
        var name: String = "",
        var degree: String = "",
        var schoolYear: String = "",
        var department: String = "",
        var language: String = "",
        var duration: String = ""
) : Serializable