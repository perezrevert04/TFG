package com.example.proyectonfc

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_report_filter.*
import java.text.SimpleDateFormat
import java.util.*


class ReportFilterActivity : AppCompatActivity() {

    companion object { const val REQ_CODE = 1312 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_filter)

        val date = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            Calendar.getInstance()[Calendar.YEAR] = year
            Calendar.getInstance()[Calendar.MONTH] = monthOfYear
            Calendar.getInstance()[Calendar.DAY_OF_MONTH] = dayOfMonth
            actualizarInput()
        }

        textViewDate.setOnClickListener {
            DatePickerDialog(this, date, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
        }

    }

    private fun actualizarInput() {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        textViewDate.text = sdf.format(Calendar.getInstance().time)
    }

}