package com.example.proyectonfc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.clases.RegistroAlumnos
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import harmony.java.awt.Color
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CreacionParte : AppCompatActivity() {

    lateinit var listaIdentificadores: MutableList<String>

    lateinit var nombreprofesor: String
    lateinit var asignatura: String
    lateinit var nombre: String
    lateinit var titulacion: String
    lateinit var grupo: String
    lateinit var curso: String
    lateinit var gestoria: String
    lateinit var idioma: String
    lateinit var duracion: String
    lateinit var horaInicio: String
    lateinit var aula: String
    lateinit var dniprofesor: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recibirDatos()
        setContentView(R.layout.activity_creacion_parte)

        val buttonCrearPdf = findViewById<Button>(R.id.buttonCrearPdf)
        buttonCrearPdf.setOnClickListener {
            try {
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val fecha = sdf.format(Date())

                val nombre_documento = "ParteFirmas-" + asignatura + "-" + grupo + "-" + fecha.replace('/', '-') + ".pdf"

                generarPdf(nombre_documento, fecha)

                toast("Se creo tu archivo pdf")

                val file = "/storage/emulated/0/Download/ParteFirmasUPV/$nombre_documento"

                val builder = VmPolicy.Builder()
                StrictMode.setVmPolicy(builder.build())


                val fileIn = File(file)
                val u = Uri.fromFile(fileIn)
                val pdfOpenintent = Intent(Intent.ACTION_VIEW)
                pdfOpenintent.setDataAndType(u, "application/pdf")
                pdfOpenintent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(pdfOpenintent)
            } catch (e: Exception) {
                toast("No se ha podido crear el archivo pdf")
            }
        }
    }

    fun recibirDatos() {
        listaIdentificadores = intent.getStringArrayListExtra("listaIdentificadores")

        nombreprofesor = intent.getStringExtra("nombreprofesor")
        asignatura = intent.getStringExtra("asignatura")
        nombre = intent.getStringExtra("nombre")
        titulacion = intent.getStringExtra("titulacion")
        grupo = intent.getStringExtra("grupo")
        curso = intent.getStringExtra("curso")
        gestoria = intent.getStringExtra("gestoria")
        idioma = intent.getStringExtra("idioma")
        duracion = intent.getStringExtra("duracion")
        horaInicio = intent.getStringExtra("horaInicio")
        aula = intent.getStringExtra("aula")
        dniprofesor = intent.getStringExtra("dniprofesor")
    }

    fun generarPdf(nombre_documento: String, fecha: String) {
        val documento = Document()


        try {

            //Creación archivo pdf
            val f = crearFichero(nombre_documento)
            val ficheroPdf = FileOutputStream(f.absolutePath)
            val writer = PdfWriter.getInstance(documento, ficheroPdf)


            // Incluimos el pie de pagina y una cabecera

            // Incluimos el pie de pagina y una cabecera
            val cabecera = HeaderFooter(Phrase("Parte de firmas Universidad Politécnica de Valencia"), false)
            cabecera.setAlignment(Element.ALIGN_CENTER)
            val pie = HeaderFooter(Phrase("Parte de firmas Universidad Politécnica de Valencia"), false)
            pie.setAlignment(Element.ALIGN_CENTER)

            documento.setHeader(cabecera)
            documento.setFooter(pie)

            // Abrimos el documento.
            documento.open()


            val font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20f,
                    Font.BOLD, Color.BLACK)
            documento.add(Paragraph("     SEGUIMIENTO DE LAS ACTIVIDADES DOCENTES\n\n\n", font))

            // Añadimos un titulo con la fuente por defecto.
            documento.add(Paragraph(""))


            val tablaA = PdfPTable(1)
            tablaA.widthPercentage = 100.00f
            tablaA.addCell("\nEspacio: $aula\n\n")
            documento.add(tablaA)


            val tablaB = PdfPTable(2)
            tablaB.widthPercentage = 100.00f
            tablaB.addCell("\nAsignatura: $asignatura-$nombre \nTitulación: $titulacion\nGrupo: $grupo\n\n")
            tablaB.addCell("\nNombre Profesor: $nombreprofesor\nDNI: $dniprofesor\n\n")
            val tablaC = PdfPTable(3)
            tablaC.widthPercentage = 100.00f
            tablaC.addCell("\nCurso/Sem.: $curso\nER Gestora: $gestoria\nIdioma: $idioma\n\n")
            tablaC.addCell("\nFecha: $fecha\nHora: $horaInicio\nDuración: $duracion\n\n")
            tablaC.addCell("Firma: \n\n\n")
            val tablaD = PdfPTable(1)
            tablaD.widthPercentage = 100.00f
            tablaD.addCell("\nObservaciones: \n\n\n")

            documento.add(tablaB)
            documento.add(tablaC)
            documento.add(tablaD)

            documento.add(Paragraph("\n\n\n"))


            // Insertamos una tabla.
            val tabla1 = PdfPTable(3)
            tabla1.widthPercentage = 100.00f
            tabla1.addCell("IDENTIFICADOR ")
            tabla1.addCell("DNI")
            tabla1.addCell("NOMBRE")


            val tabla2 = PdfPTable(3)
            tabla2.widthPercentage = 100.00f

            listaIdentificadores.forEach { tabla2.addCell(it) }
            listaIdentificadores.forEach { tabla2.addCell(it) }
            listaIdentificadores.forEach { tabla2.addCell(it) }

            documento.add(tabla1)
            documento.add(tabla2)
        } catch (e: Exception) {
            toast("No se ha podido crear el archivo pdf")
        } finally {
            documento.close();
        }
    }

    fun crearFichero(nombreFichero: String): File {
        val ruta = getRuta()
        var fichero: File? = null
        if (ruta != null) fichero = File(ruta, nombreFichero)
        return fichero!!
    }

    fun getRuta(): File? {

        // El fichero sera almacenado en un directorio dentro del directorio Descargas
        var ruta: File? = null
        if (Environment.MEDIA_MOUNTED == Environment
                        .getExternalStorageState()) {
            ruta = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ParteFirmasUPV")
            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null
                    }
                }
            }
        }

        return ruta
    }
}