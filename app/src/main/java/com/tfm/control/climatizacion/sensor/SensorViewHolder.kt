package com.tfm.control.climatizacion.sensor

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Sensor

class SensorViewHolder(view: View) : ViewHolder(view) {
    private val txtName: TextView = view.findViewById(R.id.sensor_name)
    private val txtTemperature: TextView = view.findViewById(R.id.sensor_temperature)
    private val imgTemperature: ImageView = view.findViewById(R.id.sensor_state)
    private val btnDelete: ImageButton = view.findViewById(R.id.delete_sensor)

    fun render(sensor: Sensor, notifyDelete: (String) -> Unit) {
        txtName.text = sensor.name
        txtTemperature.text = sensor.temperature.toString() + " ºC"
        if (sensor.isConnected)
            imgTemperature.setImageResource(R.drawable.ic_connected)
        else
            imgTemperature.setImageResource(R.drawable.ic_disconnected)

        btnDelete.setOnClickListener {
            notifyDelete(sensor.id)
        }
    }
}