package com.tfm.control.climatizacion.plug

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.sensor.SensorsAdapter
import com.tfm.control.climatizacion.tuya.TuyaManager
import com.thingclips.smart.android.ble.builder.BleConnectBuilder
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.api.IDevListener
import com.thingclips.smart.sdk.api.IThingDevice
import com.thingclips.smart.sdk.bean.DeviceBean

class PlugsFragment : Fragment() {
    private val devices = HashMap<String, IThingDevice>()

    private lateinit var rvPlugs: RecyclerView
    private lateinit var plugsAdapter: PlugsAdapter
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var tuyaManager: TuyaManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tuyaManager = TuyaManager.getInstance(requireActivity().application)

    }

    override fun onDestroy() {
        super.onDestroy()
        for ((_, value) in devices) {
            value.unRegisterDevListener()
            value.onDestroy()
        }
        devices.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_plugs, container, false)
        initComponent(view)
        initUI()
        return view
    }


    private fun initComponent(view: View) {
        rvPlugs = view.findViewById(R.id.rvPlugs)
        btnAdd = view.findViewById(R.id.fab_addPlug)
        btnAdd.setOnClickListener {
            tuyaManager.list()
        }

    }

    private fun initUI() {
        plugsAdapter = PlugsAdapter(ArrayList())
        rvPlugs.layoutManager =
            GridLayoutManager(context, 2) //2 columnas
        rvPlugs.adapter = plugsAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ThingHomeSdk.newHomeInstance(172730637).getHomeDetail(object : IThingHomeResultCallback {
            override fun onSuccess(bean: HomeBean?) {
                val deviceList: ArrayList<DeviceBean> = bean!!.deviceList as ArrayList<DeviceBean>
                deviceList.removeIf{d->d.deviceCategory == "wsdcg"}

                if (deviceList != null && deviceList.size > 0) {
                    val builderList: MutableList<BleConnectBuilder> = ArrayList()
                    for (deviceBean in deviceList) {
                        if (null == devices.get(deviceBean.devId) && deviceBean.deviceCategory !="wsdcg") {
                            val iThingDevice = ThingHomeSdk.newDeviceInstance(deviceBean.devId)
                            iThingDevice.registerDevListener(iDevListener)
                            devices.put(deviceBean.devId, iThingDevice)
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

                    plugsAdapter.setData(deviceList)
                    plugsAdapter.notifyDataSetChanged()
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
            if (plugsAdapter != null && plugsAdapter.plugs != null && plugsAdapter.plugs.size > 0) {
                for (item in plugsAdapter.plugs) {
                    plugsAdapter.notifyDataSetChanged()
                }
            }
        }

        override fun onRemoved(devId: String) {}

        override fun onStatusChanged(devId: String, online: Boolean) {
            if (plugsAdapter != null && plugsAdapter.plugs != null && plugsAdapter.plugs.size > 0) {
                for (item in plugsAdapter.plugs) {
                    item.isOnline = online
                    plugsAdapter.notifyDataSetChanged()
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