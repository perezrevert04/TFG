package com.example.proyectonfc.data

import com.example.proyectonfc.logic.Person

interface DatabaseDAO {

    fun addLinkedPerson(person: Person): Boolean      // Vincula a una persona con el dispositivo
    fun deviceIsLinked(): Boolean                     // Devuelve si el dispositivo tiene alguna tarjeta vinculada
    fun getLinkedPerson(): Person                     // Obtiene los datos de la persona vinculada con el dispositivo
    fun removeLinkedPerson(): Boolean                 // Elimina a la persona vinculada con el dispositivo

}