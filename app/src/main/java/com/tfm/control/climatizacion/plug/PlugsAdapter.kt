package com.tfm.control.climatizacion.plug

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Plug
import com.tfm.control.climatizacion.models.Sensor
import com.tfm.control.climatizacion.sensor.SensorViewHolder
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.bean.DeviceBean

class PlugsAdapter(var plugs: ArrayList<DeviceBean>) :
    RecyclerView.Adapter<PlugViewHolder>() {
    fun setData(list: ArrayList<DeviceBean>) {
        plugs = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlugViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plug, parent, false)

        val viewholder=  PlugViewHolder(view)
        return viewholder
    }

    override fun getItemCount() = plugs.size

    override fun onBindViewHolder(holder: PlugViewHolder, position: Int) {
        val deviceBean = plugs[position]
        val dps = ThingHomeSdk.getDataInstance().getDps(deviceBean.devId)
        var stateDps = dps!!["1"]
        var state = false;
        if (stateDps is Boolean) {
            state = stateDps
        }
        val plug = Plug(deviceBean.getName(), state, deviceBean.isOnline)
        holder.render(plug)

    }
}