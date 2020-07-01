package com.example.proyectonfc

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.logic.nearby.Discover
import kotlinx.android.synthetic.main.activity_student_sign.*


class StudentSignActivity : AppCompatActivity() {

    private lateinit var discover: Discover

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign)

        discover = Discover(this, android.os.Build.MODEL, applicationContext.packageName)

        discover.addObserver { updateAdapter() }
        discover.scanSystemIn()

        activeList.setOnItemClickListener { _: AdapterView<*>?, _: View, pos: Int, _: Long ->
            val keys = ArrayList<String>(discover.map.keys)
            discover.sendPayload(keys[pos], "3967203186")
        }
    }

    private fun updateAdapter() {
        activeList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList<String>(discover.map.values))

        if (discover.map.isEmpty()) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        discover.start()
    }

    override fun onPause() {
        super.onPause()
        discover.stop()
    }
}