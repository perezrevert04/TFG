package com.example.proyectonfc.presentation.teacher.report

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_attendance.*

class AttendanceActivity : AppCompatActivity() {

    companion object { const val ATTENDANCE_LIST = "ATTENDANCE_LIST" }

    private lateinit var attendanceList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)
        Slidr.attach(this)

        attendanceList = intent.getStringArrayListExtra(ATTENDANCE_LIST)
        title = "Asistentes: " + attendanceList.size
        attendance.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, attendanceList as List<*>)
    }

}