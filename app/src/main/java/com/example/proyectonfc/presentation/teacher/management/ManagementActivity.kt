package com.example.proyectonfc.presentation.teacher.management

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.model.Person
import com.example.proyectonfc.presentation.teacher.consults.ShowReportsActivity
import com.example.proyectonfc.presentation.teacher.management.subjects.Configuracion
import kotlinx.android.synthetic.main.activity_management.*

class ManagementActivity : AppCompatActivity() {

    private val linked: Person by lazy { (application as Global).linked }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)

        textViewRoleTeacher.text = linked.role.role
        textViewTeacherName.text = linked.name
        textViewTeacherDni.text = linked.dni
        textViewTeacherCard.text = linked.card
        textViewTeacherValidity.text = linked.validity
        textViewTeacherStatus.text = linked.status

        buttonManageSubjects.setOnClickListener { v: View ->
            val intent = Intent(v.context, Configuracion::class.java)
            startActivity(intent)
        }

        buttonConsultParts.setOnClickListener { v: View ->
            val intent = Intent(v.context, ShowReportsActivity::class.java)
            startActivity(intent)
        }
    }
}