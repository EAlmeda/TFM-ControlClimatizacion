package com.tfm.control.climatizacion.plug

import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Plug
import com.tfm.control.climatizacion.models.Sensor
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.api.IResultCallback
import com.thingclips.smart.sdk.bean.DeviceBean

class PlugViewHolder(view: View): ViewHolder(view) {

    private val txtName: TextView = view.findViewById(R.id.plug_name)
    private val imgConnected: ImageView = view.findViewById(R.id.plug_isConnected)
    private val switch: SwitchCompat = view.findViewById(R.id.plug_state)

    fun render(plug: Plug) {
        txtName.text = plug.name
        if (plug.isConnected)
            imgConnected.setImageResource(R.drawable.ic_connected)
        else
            imgConnected.setImageResource(R.drawable.ic_disconnected)
        switch.isChecked = plug.state

        switch.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener
            {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    turnPlug(isChecked, "")
                }
            })
    }

    private fun turnPlug(state:Boolean,deviceId:String){
        ThingHomeSdk.newHomeInstance(172730637).getHomeDetail(object : IThingHomeResultCallback {
            override fun onSuccess(bean: HomeBean?) {
                val deviceList: List<DeviceBean> = bean!!.deviceList
                val mDevice= ThingHomeSdk.newDeviceInstance(deviceList[1].devId)
                mDevice.publishDps("{\"01\": $state}", object : IResultCallback {
                    override fun onError(code: String?, error: String?) {
                    }

                    override fun onSuccess() {
                    }
                })
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
            }
        })
    }

}