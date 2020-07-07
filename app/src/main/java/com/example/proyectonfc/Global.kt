package com.example.proyectonfc

import android.app.Application
import com.example.proyectonfc.data.Database
import com.example.proyectonfc.model.Person

class Global : Application() {
    val database = Database(this)
    val linked: Person by lazy { database.getLinkedPerson() }
}