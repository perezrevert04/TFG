package com.example.proyectonfc

import android.app.Application
import android.provider.Settings
import com.example.proyectonfc.data.Database
import com.example.proyectonfc.db.DataBase as OldDatabase
import com.example.proyectonfc.model.Person

class Global : Application() {
    val oldDatabase = OldDatabase(this)
    val database = Database(this)
    val linked: Person by lazy { database.getLinkedPerson() }
    val androidId: String by lazy { Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) }
}