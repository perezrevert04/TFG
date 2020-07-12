package com.example.proyectonfc.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Report(
        var id: String = System.currentTimeMillis().toString(),
        var date: String = SimpleDateFormat("dd-MM-yyyy", Locale("es", "ES")).format( Date() ),
        var comments: String = "",
        var attendance: Int,
        var subject: Subject,
        var classroom: String,
        var group: String,
        var hour: String
) : Serializable {

    constructor(
            subject: Subject,
            group: Group
    ) : this(
            attendance = 0,
            subject = subject,
            classroom = group.classroom,
            group = group.name,
            hour = group.hour
    )

    constructor() : this(
            attendance = 0,
            subject = Subject(),
            classroom = "",
            group = "",
            hour = ""
    )

    fun increaseAttendance() { attendance ++ }

    fun getPdfName(): String {
        return id + "_" + subject.name + "_" + date + "_" + group + ".pdf"
    }

    override fun toString(): String {
        return """
            
            [${subject.code}]
            ${subject.name}
            (${group}, ${classroom})
            
            """.trimIndent()
    }

//    override fun toString(): String {
//        return "${subject.code}: ${subject.name} ($group, $classroom).\n$date - $hour [${subject.duration}]"
//    }

}