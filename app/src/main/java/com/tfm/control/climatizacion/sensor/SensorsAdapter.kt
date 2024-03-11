package com.tfm.control.climatizacion.sensor

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Sensor
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.bean.DeviceBean

class SensorsAdapter(
    var sensors: ArrayList<DeviceBean>,
    val clickSensor: (String, String) -> Unit
) :
    RecyclerView.Adapter<SensorViewHolder>() {

    fun setData(list: ArrayList<DeviceBean>) {
        sensors = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sensor, parent, false)
        val sensorViewHolder = SensorViewHolder(view)
        view.setOnClickListener {
            val device = sensors[sensorViewHolder.adapterPosition]
            clickSensor.invoke(device.getName(), device.devId)
        }
        return sensorViewHolder
    }

    override fun getItemCount() = sensors.size

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val deviceBean = sensors[position]
        val dps = ThingHomeSdk.getDataInstance().getDps(deviceBean.devId)
        var temperature = 0.0
        var temperatureDps = dps!!["1"]
        if (temperatureDps is Int) {
            temperature = temperatureDps.toDouble() / 10
        }
        val sensor = Sensor(deviceBean.getName(), temperature, deviceBean.isOnline)
        holder.render(sensor)

    }

}