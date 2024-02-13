package com.tfm.control.climatizacion.sensor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Sensor

class SensorsFragment : Fragment() {
    private var sensors = mutableListOf(
        Sensor("Salon", 15.0,true),
        Sensor("Cocina", 18.0,true),
        Sensor("Habitacion 1", 11.0,false),
        Sensor("Habitacion 2", 13.0,true),
    )

    private lateinit var rvSensors: RecyclerView
    private lateinit var sensorsAdapter: SensorsAdapter

    private  val ARG_OBJECT = "page"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_sensors, container, false)
        initComponent(view)
        initUI()
        return view
    }
    private fun initComponent(view:View) {
        rvSensors = view.findViewById(R.id.rvSensors)
    }

    private fun initUI() {
        sensorsAdapter = SensorsAdapter(sensors)
        rvSensors.layoutManager =
            GridLayoutManager(context,2) //2 columnas
        rvSensors.adapter = sensorsAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
        }
    }

}