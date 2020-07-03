package com.example.proyectonfc.presentation.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.model.Person
import kotlinx.android.synthetic.main.activity_manage_student_data.*

class ManageStudentDataActivity : AppCompatActivity() {

    private val linked: Person by lazy { (application as Global).linked }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_student_data)

        this.textViewRole3.text = linked.role.role
        this.textViewStudentIdentifier3.text = linked.identifier
        this.textViewStudentName.text = linked.name
        this.textViewStudentDni.text = linked.dni
        this.textViewStudentCard.text = linked.card
        this.textViewStudentValidity.text = linked.validity
        this.textViewStudentStatus.text = linked.status
    }

}