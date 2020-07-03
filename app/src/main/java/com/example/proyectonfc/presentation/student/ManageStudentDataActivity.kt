package com.example.proyectonfc.presentation.student

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.model.Person

class ManageStudentDataActivity : AppCompatActivity() {

    private val linked: Person by lazy { (application as Global).linked }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_student_data)

        Log.d("AppLog", linked.toString())
    }

}