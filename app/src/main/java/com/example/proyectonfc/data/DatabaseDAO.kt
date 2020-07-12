package com.example.proyectonfc.data

import com.example.proyectonfc.model.Person
import com.example.proyectonfc.model.Report
import com.example.proyectonfc.model.ReportFilter

interface DatabaseDAO {

    fun addLinkedPerson(person: Person): Boolean                  // Vincula a una persona con el dispositivo
    fun deviceIsLinked(): Boolean                                 // Devuelve si el dispositivo tiene alguna tarjeta vinculada
    fun getLinkedPerson(): Person                                 // Obtiene los datos de la persona vinculada con el dispositivo
    fun removeLinkedPerson(): Boolean                             // Elimina a la persona vinculada con el dispositivo

    fun addReport(report: Report): Boolean                        // Añade un nuevo parte de firmas
    fun getReportById(id: String): Report                         // Obtiene el parte de firmas con el identificador id
    fun getAllReports(): ArrayList<Report>                        // Obtiene todos los partes de firmas
    fun filterReports(filter: ReportFilter): ArrayList<Report>    // Filtra los partes de firmas
    fun removeReport(id: String): Boolean                         // Elimina el parte de firmas con el identificador id

}