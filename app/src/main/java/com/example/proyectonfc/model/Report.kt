package com.example.proyectonfc.model

import java.io.Serializable

class Report(
        var id: String = "",
        var teacher: String = "",
        var subjectCode: String = "",
        var subjectName: String = "",
        var group: String = "",
        var classroom: String = "",
        var date: String = "",
        var hour: String = "",
        var duration: String = "",
        var attendance: Int = 0,
        var comments: String = ""
) : Serializable {

    override fun toString(): String {
        return "$subjectCode: $subjectName ($group, $classroom).\n$date - $hour [$duration]"
    }

}