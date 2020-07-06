package com.example.proyectonfc.presentation.teacher.management.subjects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.db.DataBase
import com.example.proyectonfc.model.Group
import kotlinx.android.synthetic.main.activity_edit_group.*
import org.jetbrains.anko.toast

class EditGroupActivity : AppCompatActivity() {

    private lateinit var group: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_group)

        group = intent.getSerializableExtra("Group") as Group

        title = group.code
        textViewGroupName.text = group.name
        editTextGroupHour.setText(group.hour)
        editTextGroupClassroom.setText(group.classroom)

        buttonUpdateGroup.setOnClickListener {
            group.hour = editTextGroupHour.text.toString()
            group.classroom = editTextGroupClassroom.text.toString()

            DataBase(applicationContext).updateGroup(group)
            toast("¡Grupo actualizado!")
            onBackPressed()
        }
    }
}