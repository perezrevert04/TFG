package com.example.proyectonfc.logic

import java.io.Serializable

class Report(
        var id: Int = 0,
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