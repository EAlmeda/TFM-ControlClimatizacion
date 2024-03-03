package com.tfm.control.climatizacion.sensor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.tuya.TuyaManager
import com.thingclips.smart.android.ble.builder.BleConnectBuilder
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.api.IDevListener
import com.thingclips.smart.sdk.api.IThingDevice
import com.thingclips.smart.sdk.bean.DeviceBean


class SensorsFragment() : Fragment() {
    private var sensors = ArrayList<DeviceBean>()
    private val iThingDeviceHashMap = HashMap<String, IThingDevice>()
//    sensors.add
//        Sensor("Salon", 15.0, true),
//        Sensor("Cocina", 18.0, true),
//        Sensor("Habitacion 1", 11.0, false),
//        Sensor("Habitacion 2", 13.0, true),
//    )

    private lateinit var rvSensors: RecyclerView
    private lateinit var sensorsAdapter: SensorsAdapter
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var tuyaManager: TuyaManager

    private val ARG_OBJECT = "page"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tuyaManager = TuyaManager.getInstance(requireActivity().application)

    }

    override fun onDestroy() {
        super.onDestroy()
        for ((_, value) in iThingDeviceHashMap) {
            value.unRegisterDevListener()
            value.onDestroy()
        }
        iThingDeviceHashMap.clear()
    }

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
    private fun initComponent(view: View) {
        rvSensors = view.findViewById(R.id.rvSensors)
        btnAdd = view.findViewById(R.id.fab_addSensor)
        btnAdd.setOnClickListener {
            tuyaManager.list()
        }

    }

    private fun initUI() {
        sensorsAdapter = SensorsAdapter(sensors)
        rvSensors.layoutManager =
            GridLayoutManager(context, 2) //2 columnas
        rvSensors.adapter = sensorsAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
        }

        val homeId = 172730637
        ThingHomeSdk.newHomeInstance(172730637).getHomeDetail(object : IThingHomeResultCallback {
            override fun onSuccess(bean: HomeBean?) {
                val deviceList: ArrayList<DeviceBean> = bean!!.deviceList as ArrayList<DeviceBean>

                if (deviceList != null && deviceList.size > 0) {
                    val builderList: MutableList<BleConnectBuilder> = ArrayList()
                    for (deviceBean in deviceList) {
                        if (null == iThingDeviceHashMap.get(deviceBean.devId)) {
                            val iThingDevice = ThingHomeSdk.newDeviceInstance(deviceBean.devId)
                            iThingDevice.registerDevListener(iDevListener)
                            iThingDeviceHashMap.put(deviceBean.devId, iThingDevice)
                        }
                        if (deviceBean.isBluetooth) {
                            val builder = BleConnectBuilder()
                            builder.setDevId(deviceBean.devId)
                            builderList.add(builder)
                        }
                    }
                    if (builderList.size > 0) {
                        ThingHomeSdk.getBleManager().connectBleDevice(builderList)
                    }

                    sensorsAdapter.setData(deviceList)
                    sensorsAdapter.notifyDataSetChanged()
                }
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                if (errorCode == "USER_SESSION_LOSS")
                    println(errorMsg)

                // do something
            }
        })
    }


    val iDevListener: IDevListener = object : IDevListener {
        override fun onDpUpdate(devId: String, dpStr: String) {
            if (sensorsAdapter != null && sensorsAdapter.sensors != null && sensorsAdapter.sensors.size > 0) {
                for (item in sensorsAdapter.sensors) {
                    sensorsAdapter.notifyDataSetChanged()
                }
            }
        }

        override fun onRemoved(devId: String) {}

        override fun onStatusChanged(devId: String, online: Boolean) {
            if (sensorsAdapter != null && sensorsAdapter.sensors != null && sensorsAdapter.sensors.size > 0) {
                for (item in sensorsAdapter.sensors) {
                    item.isOnline = online
                    sensorsAdapter.notifyDataSetChanged()
                }
            }
        }

        override fun onNetworkStatusChanged(devId: String, status: Boolean) {
            println(status)
        }

        override fun onDevInfoUpdate(devId: String) {
            println(devId)
        }
    }



}