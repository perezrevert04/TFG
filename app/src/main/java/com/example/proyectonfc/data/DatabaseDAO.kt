package com.example.proyectonfc.data

import com.example.proyectonfc.logic.Person
import com.example.proyectonfc.logic.Report
import com.example.proyectonfc.logic.ReportFilter

interface DatabaseDAO {

    fun addLinkedPerson(person: Person): Boolean                  // Vincula a una persona con el dispositivo
    fun deviceIsLinked(): Boolean                                 // Devuelve si el dispositivo tiene alguna tarjeta vinculada
    fun getLinkedPerson(): Person                                 // Obtiene los datos de la persona vinculada con el dispositivo
    fun removeLinkedPerson(): Boolean                             // Elimina a la persona vinculada con el dispositivo

    fun addReport(report: Report): Boolean                        // Añade un nuevo parte de firmas
    fun getReportById(id: String): Report                         // Obtiene el parte de firmas con el identificador id
    fun getAllReports(): ArrayList<Report>                        // Obtiene todos los partes de firmas
    fun filterReports(filter: ReportFilter): ArrayList<Report>    // Filtra los partes de firmas
    fun removeReport(id: Int): Boolean                            // Elimina el parte de firmas con el identificador id

}