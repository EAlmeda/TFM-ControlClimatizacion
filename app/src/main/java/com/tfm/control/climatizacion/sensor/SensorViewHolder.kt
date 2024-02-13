package com.tfm.control.climatizacion.sensor

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Sensor

class SensorViewHolder(view: View) :ViewHolder(view) {
    private val txtName: TextView =view.findViewById(R.id.sensor_name)
    private val txtTemperature: TextView =view.findViewById(R.id.sensor_temperature)

    fun render(sensor: Sensor){
        txtName.text = sensor.name
        txtTemperature.text = sensor.temperature.toString() + " ºC"
    }
}