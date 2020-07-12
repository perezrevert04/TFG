package com.example.proyectonfc.logic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.example.proyectonfc.model.Person
import com.example.proyectonfc.model.Report
import com.example.proyectonfc.model.Student
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import harmony.java.awt.Color
import java.io.File
import java.io.FileOutputStream

const val FOLDER_NAME = "Parte Firmas UPV"
const val PDF_HEADER = "Universitat Politècnica de València"

class PdfManager(val report: Report, val teacher: Person, val list: List<Student>) {

    private val font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20f, Font.BOLD, Color.BLACK)
    private val downloadPath by lazy { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) }

    fun create() {
        val document = Document()

        try {
            val file = createFile( report.getPdfName() )
            val fos = FileOutputStream(file.absolutePath)
            PdfWriter.getInstance(document, fos)

            val headerFooter = generateHeaderFooter()
            document.setHeader(headerFooter)
            document.setFooter(headerFooter)

            document.open()
            
            document.add(Paragraph("     SEGUIMIENTO DE LAS ACTIVIDADES DOCENTES\n\n\n", font))
            document.add(Paragraph(""))


            val tablaA = PdfPTable(1)
            tablaA.widthPercentage = 100.00f
            tablaA.addCell("\nEspacio: ${report.classroom}\n\n")
            document.add(tablaA)

            val tablaB = PdfPTable(2)
            tablaB.widthPercentage = 100.00f
            tablaB.addCell("\nAsignatura: ${report.subject.code}-${report.subject.name} \nTitulación: ${report.subject.degree}\nGrupo: ${report.group}\n\n")
            tablaB.addCell("\nProfesor: ${teacher.name}\nDNI: ${teacher.dni}\n\n")
            val tablaC = PdfPTable(3)
            tablaC.widthPercentage = 100.00f
            tablaC.addCell("\nCurso/Sem.: ${report.subject.schoolYear}\nER Gestora: ${report.subject.department}\nIdioma: ${report.subject.language}\n\n")
            tablaC.addCell("\nFecha: ${report.date}\nHora: ${report.hour}\nDuración: ${report.subject.duration}\n\n")
            tablaC.addCell("Firma: \n\n\n")
            val tablaD = PdfPTable(1)
            tablaD.widthPercentage = 100.00f
            tablaD.addCell("\nObservaciones: ${report.comments}\n\n\n")

            document.add(tablaB)
            document.add(tablaC)
            document.add(tablaD)

            document.add(Paragraph("\n\n\n"))

            val tabla1 = PdfPTable(3)
            tabla1.widthPercentage = 100.00f
            tabla1.addCell("Identificador")
            tabla1.addCell("DNI")
            tabla1.addCell("Nombre")


            val tabla2 = PdfPTable(3)
            tabla2.widthPercentage = 100.00f

            list.forEach {
                tabla2.addCell(it.id)
                tabla2.addCell(it.dni)
                tabla2.addCell(it.name)
            }

            document.add(tabla1)
            document.add(tabla2)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            document.close()
        }
    }

    fun open(activity: Activity) {
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val fileIn = createFile(report.getPdfName())
        val uri = Uri.fromFile(fileIn)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    private fun createFile(filename: String): File {
        val path = File(downloadPath, FOLDER_NAME).path
        File(path).mkdir() // Crea carpeta "Parte Firmas UPV" si no existe
        return File(path, filename)
    }

    private fun generateHeaderFooter(): HeaderFooter {
        val header = HeaderFooter( Phrase(PDF_HEADER), false )
        header.setAlignment(Element.ALIGN_CENTER)

        return header
    }
}