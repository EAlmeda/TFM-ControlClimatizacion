package com.tfm.control.climatizacion.sensor.bind

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.tfm.control.climatizacion.R
import com.thingclips.smart.android.ble.api.BleScanResponse
import com.thingclips.smart.android.ble.api.LeScanSetting
import com.thingclips.smart.android.ble.api.ScanDeviceBean
import com.thingclips.smart.android.ble.api.ScanType
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IMultiModeActivatorListener
import com.thingclips.smart.sdk.api.IThingActivatorGetToken
import com.thingclips.smart.sdk.bean.DeviceBean
import com.thingclips.smart.sdk.bean.MultiModeActivatorBean

class BindDeviceFragment : DialogFragment() {
    private lateinit var btnCancel: Button
    private lateinit var btnBind: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var txtEncontrado: TextView
    private lateinit var txtWifiName: TextInputLayout
    private lateinit var txtWifiPassword: TextInputLayout

    private var deviceBean: ScanDeviceBean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bind_device, container, false)
        initComponent(view)
        searchDevices()
        return view
    }

    private fun initComponent(view: View) {
        btnBind = view.findViewById(R.id.btnBind)
        btnCancel = view.findViewById(R.id.btnCancel)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnBind.setOnClickListener {
            bindDevice()
        }

        progressBar = view.findViewById(R.id.progressBar)
        txtEncontrado = view.findViewById(R.id.txtEncontrado)

        txtWifiName = view.findViewById(R.id.txtNameWifi)
        txtWifiPassword = view.findViewById(R.id.txtPasswordWifi)

    }

    private fun searchDevices() {
        val scanSetting = LeScanSetting.Builder()
            .setTimeout(500000) // The duration of the scanning. Unit: milliseconds.
            .addScanType(ScanType.SINGLE) // ScanType.SINGLE: scans for Bluetooth LE devices.
            .addScanType(ScanType.SIG_MESH) // ScanType.SINGLE: scans for Bluetooth LE devices.
            .addScanType(ScanType.MESH) // ScanType.SINGLE: scans for Bluetooth LE devices.
            .build()

        ThingHomeSdk.getBleOperator().startLeScan(scanSetting, object : BleScanResponse {
            override fun onResult(bean: ScanDeviceBean?) {
                updateView(bean)
                deviceBean = bean


            }
        })
    }

    private fun updateView(bean: ScanDeviceBean?) {
        txtEncontrado.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        txtEncontrado.text = "Dispositivo \"${bean!!.name}\" encontrado"
        if (!txtWifiName.editText!!.text.toString()
                .isNullOrEmpty() && !txtWifiPassword.editText!!.text.toString().isNullOrEmpty()
        ) {
            btnBind.isEnabled = true
        }
    }

    private fun bindDevice() {
        txtWifiName.visibility = View.GONE
        txtWifiPassword.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        txtEncontrado.visibility = View.GONE
        ThingHomeSdk.getActivatorInstance().getActivatorToken(
            172730637,
            object : IThingActivatorGetToken {
                override fun onSuccess(token: String?) {
                    activate(deviceBean!!, token, 172730637)
                }

                override fun onFailure(s: String?, s1: String?) {
                    Toast.makeText(
                        context,
                        "ERROR AL OBTENER TOKEN",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    dismiss()
                }
            })
    }

    private fun activate(mScanDeviceBean: ScanDeviceBean, token: String?, homeId: Long) {
        var multiModeActivatorBean = MultiModeActivatorBean(mScanDeviceBean);
// mScanDeviceBean: returned by ScanDeviceBean in the scanning result callback.
        multiModeActivatorBean.deviceType = mScanDeviceBean.deviceType; // The type of device.
        multiModeActivatorBean.uuid = mScanDeviceBean.uuid; // The UUID of the device.
        multiModeActivatorBean.address = mScanDeviceBean.address; // The IP address of the device.
        multiModeActivatorBean.mac = mScanDeviceBean.mac; // The MAC address of the device.
        multiModeActivatorBean.ssid =
            txtWifiName.editText?.text.toString() // The SSID of the target Wi-Fi network.
        multiModeActivatorBean.pwd = txtWifiPassword.editText?.text.toString()
        multiModeActivatorBean.token = token; // The pairing token.
        multiModeActivatorBean.homeId = homeId; // The value of `homeId` for the current home.
        multiModeActivatorBean.timeout = 100000; // The timeout value.
        ThingHomeSdk.getActivator().newMultiModeActivator()
            .startActivator(multiModeActivatorBean, object : IMultiModeActivatorListener {
                override fun onSuccess(deviceBean: DeviceBean?) {
                    println(deviceBean)
                    Toast.makeText(
                        context,
                        "DISPOSITIVO VINCULADO",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    dismiss()
                }

                override fun onFailure(code: Int, msg: String, handle: Any) {
                    Toast.makeText(
                        context,
                        "ERROR AL VINCULAR DISPOSITIVO",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    dismiss()
                }

            })
    }
}