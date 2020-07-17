package com.example.proyectonfc.presentation.teacher.management.consults

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.R
import com.example.proyectonfc.model.ReportFilter
import kotlinx.android.synthetic.main.activity_report_filter.*
import java.text.SimpleDateFormat
import java.util.*

class ReportFilterActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FILTER = "EXTRA_FILTER"
        const val REQ_CODE = 1312
    }
    private val calendar: Calendar = Calendar.getInstance()

    private lateinit var filter: ReportFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_filter)

        filter = intent.getSerializableExtra(EXTRA_FILTER) as ReportFilter
        editTextSubject.setText(filter.subject)
        editTextGroup.setText(filter.group)
        editTextDate.setText(filter.date)

        val date = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = monthOfYear
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateInput()
        }

        editTextDate.setOnClickListener {
            DatePickerDialog(this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        fab_confirm.setOnClickListener {
            filter.subject = editTextSubject.text.toString().trim()
            filter.group = editTextGroup.text.toString().trim()
            filter.date = editTextDate.text.toString().trim()

            intent.putExtra(EXTRA_FILTER, filter)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        buttonClear.setOnClickListener { clearFilters() }

    }

    private fun updateInput() {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale("es", "ES"))
        editTextDate.setText(sdf.format(calendar.time))
    }

    private fun clearFilters() {
        filter = ReportFilter()

        editTextSubject.setText("")
        editTextGroup.setText("")
        editTextDate.setText("")
    }

}