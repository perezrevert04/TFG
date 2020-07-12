package com.example.proyectonfc.presentation.teacher.report

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.clases.AddComment
import com.example.proyectonfc.db.DataBase
import com.example.proyectonfc.logic.ReportManager
import com.example.proyectonfc.model.Person
import com.example.proyectonfc.model.Report
import com.example.proyectonfc.model.Student
import com.example.proyectonfc.util.biometric.Biometry
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import harmony.java.awt.Color
import kotlinx.android.synthetic.main.activity_creacion_parte.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream

class CreacionParte : AppCompatActivity() {

    companion object { const val REQ_CODE = 1213 }

    private val database by lazy { (application as Global).database }

    private lateinit var biometry: Biometry

    private lateinit var listaIdentificadores: MutableList<String>
    private lateinit var report: Report
    private lateinit var person: Person

    private lateinit var manager: ReportManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creacion_parte)

        biometry = Biometry(this, title = "Autenticación", subtitle = "Identifíquese para generar el pdf.")
        manager = ReportManager(this, database)
        getData()

        buttonAddComment.setOnClickListener {
            val intent = Intent(this, AddComment::class.java)
            intent.putExtra("comments", report.comments)
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
                    database.consultarAlumno(report.subject.code, it)
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
            report.comments = data?.extras?.getString("comments") ?: ""
            textViewComments.text = report.comments
            textViewEmptyComments.visibility = if (report.comments.isEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun getData() {

        report = intent.getSerializableExtra("ReportObject") as Report
        person = database.getLinkedPerson()
        listaIdentificadores = intent.getStringArrayListExtra("listaIdentificadores")

        val subject = "${report.subject.code}: ${report.subject.name}"
        val teacher = "${person.name} (${person.dni})"

        textViewFecha.text = report.date
        textViewCount.text = report.attendance.toString()
        textViewSubject.text = subject
        textViewTeacher.text = teacher
        textViewGroup.text = report.group
        textViewClassroom.text = report.classroom
        textViewHour.text = report.hour
        textViewDuration.text = report.subject.duration

    }

    private fun finalizarParte() {
        saveReport()

        try {
            val filename = report.getPdfName()

            generarPdf(filename, report.date)

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


            val font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20f, Font.BOLD, Color.BLACK)
            documento.add(Paragraph("     SEGUIMIENTO DE LAS ACTIVIDADES DOCENTES\n\n\n", font))

            // Añadimos un titulo con la fuente por defecto.
            documento.add(Paragraph(""))


            val tablaA = PdfPTable(1)
            tablaA.widthPercentage = 100.00f
            tablaA.addCell("\nEspacio: ${report.classroom}\n\n")
            documento.add(tablaA)


            val tablaB = PdfPTable(2)
            tablaB.widthPercentage = 100.00f
            tablaB.addCell("\nAsignatura: ${report.subject.code}-${report.subject.name} \nTitulación: ${report.subject.degree}\nGrupo: ${report.group}\n\n")
            tablaB.addCell("\nProfesor: ${person.name}\nDNI: ${person.dni}\n\n")
            val tablaC = PdfPTable(3)
            tablaC.widthPercentage = 100.00f
            tablaC.addCell("\nCurso/Sem.: ${report.subject.schoolYear}\nER Gestora: ${report.subject.department}\nIdioma: ${report.subject.language}\n\n")
            tablaC.addCell("\nFecha: $date\nHora: ${report.hour}\nDuración: ${report.subject.duration}\n\n")
            tablaC.addCell("Firma: \n\n\n")
            val tablaD = PdfPTable(1)
            tablaD.widthPercentage = 100.00f
            tablaD.addCell("\nObservaciones: ${report.comments}\n\n\n")

            documento.add(tablaB)
            documento.add(tablaC)
            documento.add(tablaD)

            documento.add(Paragraph("\n\n\n"))


            // Insertamos una tabla.
            val tabla1 = PdfPTable(3)
            tabla1.widthPercentage = 100.00f
            tabla1.addCell("Identificador")
            tabla1.addCell("DNI")
            tabla1.addCell("Nombre")


            val tabla2 = PdfPTable(3)
            tabla2.widthPercentage = 100.00f

            val database = DataBase(applicationContext)
            listaIdentificadores.forEach {
                database.consultarAlumno(report.subject.code, it)
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
            documento.close()
        }
    }

    private fun crearFichero(filename: String): File {
        val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ParteFirmasUPV").path
        File(path).mkdir()
        return File(path, filename)
    }

    private fun saveReport() {
        /* TODO: Cambiar listado predefinido por el listado real de los alumnos */
        val list = arrayListOf<Student>()
        list.add(Student(id = "3967203186", dni = "20458644", name = "Carles Perez Revert"))
        list.add(Student(id = "1234567890", dni = "23414324", name = "Juan Ignacio Delgado Alemany"))
        manager.saveReport(report, list)
    }

}