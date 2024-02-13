package com.tfm.control.climatizacion.sensor

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Sensor

class SensorsAdapter(private val sensors:List<Sensor>) : RecyclerView.Adapter<SensorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sensor, parent, false)

        return SensorViewHolder(view)
    }

    override fun getItemCount() = sensors.size

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        holder.render(sensors[position])

    }
}