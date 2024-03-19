package com.tfm.control.climatizacion.sensor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Condition
import com.tfm.control.climatizacion.models.DatabaseHelper
import com.tfm.control.climatizacion.models.Routine
import com.tfm.control.climatizacion.sensor.routine.NewRoutineActivity
import com.tfm.control.climatizacion.sensor.routine.RoutineAdapter

class SensorDetailActivity : AppCompatActivity() {

    private lateinit var sensorName: String
    private lateinit var sensorId: String
    private var routines = ArrayList<Routine>()
    private lateinit var rvRoutines: RecyclerView
    private lateinit var routineAdapter: RoutineAdapter
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var db: DatabaseHelper
    private lateinit var emptyListTxt: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(this)

        checkExtras()
        setContentView(R.layout.activity_sensor_detail)
        initComponent()
        initUI()
    }

    private fun initComponent() {
        rvRoutines = findViewById(R.id.rvRoutines)
        btnAdd = findViewById(R.id.fab_addRoutine)
        emptyListTxt = findViewById(R.id.empty_view)
        btnAdd.setOnClickListener {
            val intent = Intent(this, NewRoutineActivity::class.java)
            intent.putExtra("sensorId", sensorId)
            startActivity(intent)
        }
    }

    private fun initUI() {
        routines = db.getAllRoutines(sensorId)
        checkEmpty(routines.size)


        routineAdapter = RoutineAdapter(routines, this::checkEmpty)
        rvRoutines.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvRoutines.adapter = routineAdapter
    }

    private fun checkEmpty(size: Int) {
        if (size <= 0) {
            rvRoutines.visibility = View.GONE
            emptyListTxt.visibility = View.VISIBLE;
        } else {
            rvRoutines.visibility = View.VISIBLE
            emptyListTxt.visibility = View.GONE;
        }
    }

    private fun checkExtras() {
        this.sensorName = intent.extras?.getString("sensorName") as String
        this.sensorId = intent.extras?.getString("sensorId") as String
    }

    override fun onResume() {
        super.onResume()
        routines = db.getAllRoutines(sensorId)
        checkEmpty(routines.size)
        routineAdapter.setData(routines)
        routineAdapter.notifyDataSetChanged()
    }
}