package com.tfm.control.climatizacion.sensor.routine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tfm.control.climatizacion.R

class NewRoutineActivity : AppCompatActivity() {
    private lateinit var sensorId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_routine)
        this.sensorId = intent.extras?.getString("sensorId") as String
        navigateToTemperatureFragment()
    }

    private fun navigateToTemperatureFragment (){
        val temperatureFragment  = TemperatureConditionFragment.newInstance(sensorId)
        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.routine_fragment_container, temperatureFragment)
            .commit()
    }
}