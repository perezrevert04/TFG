package com.example.proyectonfc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.logic.Report
import kotlinx.android.synthetic.main.activity_creacion_parte.*
import kotlinx.android.synthetic.main.activity_show_reports.*
import org.jetbrains.anko.toast


class ShowReportsActivity : AppCompatActivity() {

    private val database by lazy { (application as Global).database }

    private var reportsList: MutableList<String> = mutableListOf()
    private lateinit var reports: ListView
    private lateinit var allReports: ArrayList<Report>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_reports)

        allReports = database.getAllReports()

        allReports.forEach { reportsList.add(it.toString()) }

        reports = findViewById<View>(R.id.listView) as ListView
        updateAdapter()

        reports.setOnItemClickListener { adapter: AdapterView<*>?, view: View, position: Int, long: Long ->

        }

        reports.setOnItemLongClickListener { _: AdapterView<*>?, _: View, position: Int, _: Long ->
            showAlert(position)
            true
        }

        fab_search.setOnClickListener {
            val intent = Intent(this, ReportFilterActivity::class.java)
            // put extra
            startActivityForResult(intent, ReportFilterActivity.REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
//            comments = data?.extras?.getString("comments") ?: ""
//            textViewComments.text = comments
//            textViewEmptyComments.visibility = if (comments.isEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun showAlert(position: Int) {
        val report = allReports[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle( report.subjectCode ).setMessage( "$report\n\n¿Desea eliminar los datos de este parte?" )



        builder.setNegativeButton("Cancelar") { _, _ -> }
        builder.setPositiveButton("Eliminar") { _, _ ->
            if (database.removeReport(report.id)) {
                allReports.removeAt(position)
                reportsList.removeAt(position)
                updateAdapter()
                toast("Eliminado correctamente")
            } else {
                toast("Ha ocurrido un problema")
            }
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun updateAdapter() {
        reports.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reportsList as List<*>)
    }
}