package com.example.proyectonfc

import android.app.Application
import com.example.proyectonfc.data.Database

class Global : Application() {
    val database = Database(this)
}