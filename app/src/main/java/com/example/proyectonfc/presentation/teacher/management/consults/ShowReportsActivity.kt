package com.example.proyectonfc.presentation.teacher.management.consults

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.Global
import com.example.proyectonfc.R
import com.example.proyectonfc.use_cases.CommandVoiceActivity
import com.example.proyectonfc.logic.ReportManager
import com.example.proyectonfc.model.Report
import com.example.proyectonfc.model.ReportFilter
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_show_reports.*
import org.jetbrains.anko.toast


class ShowReportsActivity : AppCompatActivity() {

    private var reportsList: MutableList<String> = mutableListOf()
    private lateinit var reports: ListView
    private lateinit var allReports: ArrayList<Report>

    private var filter = ReportFilter()
    private lateinit var manager: ReportManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_reports)
        Slidr.attach(this)

        manager = ReportManager(this, (application as Global).database)

        allReports = manager.getAllReports()

        allReports.forEach { reportsList.add( it.toString() ) }

        reports = findViewById<View>(R.id.listView) as ListView
        updateAdapter()

        reports.setOnItemClickListener { _: AdapterView<*>?, _: View, position: Int, _: Long ->
            val intent = Intent(this, ReportDataActivity::class.java)
            intent.putExtra(ReportDataActivity.EXTRA_REPORT, allReports[position])
            startActivity(intent)
        }

        reports.setOnItemLongClickListener { _: AdapterView<*>?, _: View, position: Int, _: Long ->
            showAlert(position)
            true
        }

        fab_search.setOnClickListener {
            val intent = Intent(this, ReportFilterActivity::class.java)
            intent.putExtra(ReportFilterActivity.EXTRA_FILTER, filter)
            startActivityForResult(intent, ReportFilterActivity.REQ_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_voice, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.command_voice -> {
            val intent = Intent(this, CommandVoiceActivity::class.java)
            this.startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ReportFilterActivity.REQ_CODE && resultCode == Activity.RESULT_OK) {
            filter = data?.getSerializableExtra(ReportFilterActivity.EXTRA_FILTER) as ReportFilter
            allReports = manager.filterReports(filter)
            reportsList = mutableListOf()
            allReports.forEach { reportsList.add( it.toString() ) }
            updateAdapter()

            toast("Resultados: " + reportsList.size)
        }
    }

    private fun showAlert(position: Int) {
        val report = allReports[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle( report.subject.code ).setMessage( "$report\n\n¿Desea eliminar los datos de este parte?" )

        builder.setNegativeButton("Cancelar") { _, _ -> }
        builder.setPositiveButton("Eliminar") { _, _ ->
            if (manager.removeReport(report.id)) {
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