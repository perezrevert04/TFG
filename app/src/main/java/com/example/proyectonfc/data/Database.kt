package com.example.proyectonfc.data

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.proyectonfc.logic.Person

const val LINKED_PERSON_TABLE = "linked_person_table"

class Database(context: Context) : SQLiteOpenHelper(context, "shopping_notes", null, 1), DatabaseDAO {
    override fun onCreate(db: SQLiteDatabase) {
        createLinkedPersonTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }

    override fun addLinkedPerson(person: Person): Boolean {
        if (deviceIsLinked()) return false;

        val values = ContentValues().apply {
            put("name", person.name)
            put("dni", person.dni)
            put("card", person.card)
            put("validity", person.validity)
            put("role", person.role)
            put("status", person.status)
        }

        return writableDatabase.insert(LINKED_PERSON_TABLE, null, values) > -1
    }

    override fun deviceIsLinked(): Boolean {
        val cursor = readableDatabase.rawQuery("SELECT * FROM $LINKED_PERSON_TABLE", null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }

    override fun getLinkedPerson(): Person {
        if (!deviceIsLinked()) throw SQLException("No hay tarjeta vinculada a este dispositivo")

        val cursor = readableDatabase.rawQuery("SELECT * FROM $LINKED_PERSON_TABLE", null)
        cursor.moveToFirst()

        val person = Person()
        person.name = cursor.getString(1)
        person.dni = cursor.getString(2)
        person.card = cursor.getString(3)
        person.validity = cursor.getString(4)
        person.role = cursor.getString(5)
        person.status = cursor.getString(6)

        cursor.close()

        return person
    }

    override fun removeLinkedPerson(): Boolean {
        TODO("Not yet implemented")
    }

    private fun createLinkedPersonTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE $LINKED_PERSON_TABLE (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "dni TEXT, " +
                        "card TEXT ," +
                        "validity TEXT ," +
                        "role TEXT ," +
                        "status TEXT " +
                        ")"
        )
    }

}