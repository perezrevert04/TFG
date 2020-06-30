package com.example.proyectonfc

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonfc.logic.nearby.Discover
import kotlinx.android.synthetic.main.activity_student_sign.*
import org.jetbrains.anko.toast
import java.util.HashMap


class StudentSignActivity : AppCompatActivity() {

    private lateinit var discover: Discover

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign)

        discover = Discover(this, android.os.Build.MODEL, applicationContext.packageName)

        discover.addObserver { updateAdapter() }
        discover.scanSystemIn()


        activeList.setOnItemClickListener { _: AdapterView<*>?, _: View, position: Int, _: Long ->
            val array = ArrayList<String>(discover.map.values)
            toast(array[position])
        }
    }

    private fun updateAdapter() {
        Log.d("AppLog", "Updating: " + discover.map.size)
        activeList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList<String>(discover.map.values))
//        activeList.adapter = HashMapAdapter(discover.map as HashMap<String, String>)
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