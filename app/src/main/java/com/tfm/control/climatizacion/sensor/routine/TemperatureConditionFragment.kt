package com.tfm.control.climatizacion.sensor.routine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.sensor.SensorDetailActivity

class TemperatureConditionFragment : Fragment() {
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("sensorId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_temperature_condition, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            TemperatureConditionFragment().apply {
                arguments = Bundle().apply {
                    putString("sensorId", param1)
                }
            }
    }
}