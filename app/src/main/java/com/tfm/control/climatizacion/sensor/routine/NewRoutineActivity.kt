package com.tfm.control.climatizacion.sensor.routine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.tfm.control.climatizacion.R


class NewRoutineActivity : AppCompatActivity() {
    private lateinit var sensorId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_routine)

        this.sensorId = intent.extras?.getString("sensorId") as String
        val bundle = Bundle()
        bundle.putString("sensorId", sensorId)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.routine_fragment_container) as NavHostFragment

        navHostFragment.navController.setGraph(R.navigation.routine_navigation, intent.extras)
//        findNavController(androidx.navigation.fragment.R.id.nav_host_fragment_container).setGraph(R.navigation.routine_navigation,bundle)
//        val temperatureFragment  = TemperatureConditionFragment.newInstance(sensorId)
//
//
//        this.supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.routine_fragment_container, temperatureFragment)
//            .commit()
    }
}