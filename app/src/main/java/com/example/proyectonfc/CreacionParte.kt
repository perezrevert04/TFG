package com.example.proyectonfc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.clases.AddComment
import com.example.proyectonfc.db.DataBase
import com.example.proyectonfc.logic.Person
import com.example.proyectonfc.logic.Report
import com.example.proyectonfc.logic.biometric.Biometry
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import harmony.java.awt.Color
import kotlinx.android.synthetic.main.activity_creacion_parte.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CreacionParte : AppCompatActivity() {

    companion object { const val REQ_CODE = 1213 }

    private val database by lazy { (application as Global).database }
    private lateinit var person: Person

    lateinit var listaIdentificadores: MutableList<String>

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
    var comments = ""

    private lateinit var biometry: Biometry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creacion_parte)

        biometry = Biometry(this, title = "Autenticación", subtitle = "Identifíquese para generar el pdf.")
        getData()

        buttonAddComment.setOnClickListener {
            val intent = Intent(this, AddComment::class.java)
            intent.putExtra("comments", comments)
            startActivityForResult(intent, 1234)
        }

        buttonCrearPdf.setOnClickListener {
            biometry.authenticate  { finalizarParte() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_attendance, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_attendance -> {
                val array: ArrayList<String> = arrayListOf()
                val database = DataBase(applicationContext)
                listaIdentificadores.forEach {
                    database.consultarAlumno(asignatura, it)
                    array.add("\n" + database.listDni + " - " + database.listNombre + "\n")
                }
                database.close()

                val intent = Intent(this, AttendanceActivity::class.java)
                intent.putExtra(AttendanceActivity.ATTENDANCE_LIST, array)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            comments = data?.extras?.getString("comments") ?: ""
            textViewComments.text = comments
            textViewEmptyComments.visibility = if (comments.isEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun getData() {
        person = database.getLinkedPerson()

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        textViewFecha.text = sdf.format( Date() )

        listaIdentificadores = intent.getStringArrayListExtra("listaIdentificadores")
        textViewCount.text = listaIdentificadores.size.toString()

        asignatura = intent.getStringExtra("asignatura")
        nombre = intent.getStringExtra("nombre")
        textViewSubject.text = "$asignatura: $nombre"

        textViewTeacher.text = "${person.name} (${person.dni})"

        grupo = intent.getStringExtra("grupo")
        textViewGroup.text = grupo

        aula = intent.getStringExtra("aula")
        textViewClassroom.text = aula

        horaInicio = intent.getStringExtra("horaInicio")
        textViewHour.text = horaInicio

        duracion = intent.getStringExtra("duracion")
        textViewDuration.text = duracion

        titulacion = intent.getStringExtra("titulacion")
        curso = intent.getStringExtra("curso")
        gestoria = intent.getStringExtra("gestoria")
        idioma = intent.getStringExtra("idioma")
    }

    private fun finalizarParte() {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val date = sdf.format(Date())

            val filename = asignatura + "_" + grupo + "_" + date.replace('/', '-') + "_" + horaInicio + "_" + aula + ".pdf"

            generarPdf(filename, sdf.format(Date()))

            val retIntent = Intent()
            retIntent.putExtra("filename", filename)
            setResult(Activity.RESULT_OK, retIntent)
            finish()

        } catch (e: Exception) {
            toast("No se ha podido crear el archivo pdf")
        }
    }

    fun generarPdf(filename: String, date: String) {
        val documento = Document()

        try {

            //Creación archivo pdf
            val f = crearFichero(filename)
            val ficheroPdf = FileOutputStream(f.absolutePath)
            PdfWriter.getInstance(documento, ficheroPdf)

            // Incluimos el pie de pagina y una cabecera
            val cabecera = HeaderFooter(Phrase("Parte de firmas Universitat Politècnica de València"), false)
            cabecera.setAlignment(Element.ALIGN_CENTER)
            val pie = HeaderFooter(Phrase("Parte de firmas Universitat Politècnica de València"), false)
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
            tablaB.addCell("\nProfesor: ${person.name}\nDNI: ${person.dni}\n\n")
            val tablaC = PdfPTable(3)
            tablaC.widthPercentage = 100.00f
            tablaC.addCell("\nCurso/Sem.: $curso\nER Gestora: $gestoria\nIdioma: $idioma\n\n")
            tablaC.addCell("\nFecha: $date\nHora: $horaInicio\nDuración: $duracion\n\n")
            tablaC.addCell("Firma: \n\n\n")
            val tablaD = PdfPTable(1)
            tablaD.widthPercentage = 100.00f
            tablaD.addCell("\nObservaciones: $comments\n\n\n")

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

            val database = DataBase(applicationContext)
            listaIdentificadores.forEach {
                database.consultarAlumno(asignatura, it)
                tabla2.addCell(it)
                tabla2.addCell(database.listDni)
                tabla2.addCell(database.listNombre)
            }
            database.close()

            documento.add(tabla1)
            documento.add(tabla2)
        } catch (e: Exception) {
            toast("No se ha podido crear el archivo pdf")
            Log.e("AppLog", "No se ha podido crear el pdf \n ${e.message}")
        } finally {
            createReport()
            documento.close()
        }
    }

    private fun crearFichero(filename: String): File {
        val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ParteFirmasUPV").path
        Log.d("AppLog", path)
        File(path).mkdir()
        return File(path, filename)
    }

    private fun createReport() {
        val report = Report()
        report.teacher = "${person.name} (${person.dni})"
        report.subjectCode = asignatura
        report.subjectName = nombre
        report.group = grupo
        report.classroom = aula
        report.date = textViewFecha.text.toString()
        report.hour = horaInicio
        report.duration = duracion
        report.attendance = listaIdentificadores.size
        report.comments = comments

        database.addReport(report)
    }

}