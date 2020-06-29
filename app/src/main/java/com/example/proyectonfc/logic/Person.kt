package com.example.proyectonfc.logic

import java.io.Serializable

class Person : Serializable {

    var name: String = ""
    var dni: String = ""
    var card: String = ""
    var validity: String = ""
    var role: Role = Role.STUDENT
    var status: String = ""
    var identifier: String = ""

    companion object {
        const val CARD_INFO = "CARD_INFO"

        const val DNI_TAG = "DNI:"
        const val NAME_TAG = "Nombre:"
        const val STATE_TAG = "Estado:"
        const val CARD_TAG = "Tarjeta:"
        const val VALIDITY_TAG = "Vigencia:"

        const val CSS_QUERY = "td"
    }

    override fun toString(): String {
        return "\nName: $name" +
                "\nDNI: $dni" +
                "\nCard: $card" +
                "\nValidity: $validity" +
                "\nRole: $role" +
                "\nStatus: $status"
    }
}