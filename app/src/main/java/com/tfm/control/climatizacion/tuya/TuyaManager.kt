package com.tfm.control.climatizacion.tuya

import android.app.Application
import android.widget.Toast
import com.thingclips.smart.android.ble.api.BleScanResponse
import com.thingclips.smart.android.ble.api.LeScanSetting
import com.thingclips.smart.android.ble.api.ScanDeviceBean
import com.thingclips.smart.android.ble.api.ScanType
import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.ConfigProductInfoBean
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.api.IMultiModeActivatorListener
import com.thingclips.smart.sdk.api.IResultCallback
import com.thingclips.smart.sdk.api.IThingActivatorGetToken
import com.thingclips.smart.sdk.api.IThingDataCallback
import com.thingclips.smart.sdk.bean.DeviceBean
import com.thingclips.smart.sdk.bean.MultiModeActivatorBean


class TuyaManager(private val application: Application) {
    private lateinit var instance: TuyaManager

    companion object {

        @Volatile
        private var instance: TuyaManager? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: TuyaManager(application).also { instance = it }
            }
    }

    init {
        ThingHomeSdk.setDebugMode(true);
        ThingHomeSdk.init(application);
        login()
    }


    private fun login() {
        ThingHomeSdk.getUserInstance().loginWithEmail("86", "e.almeda.t@gmail.com", "123456",
            object : ILoginCallback {
                override fun onError(code: String, error: String) {
                    Toast.makeText(
                        application.applicationContext,
                        "ERROR EN LOGIN",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onSuccess(user: User) {
                    Toast.makeText(
                        application.applicationContext,
                        "LOGIN REALIZADO CON EXITO",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
    fun bindDevice() {
        val scanSetting = LeScanSetting.Builder()
            .setTimeout(500000) // The duration of the scanning. Unit: milliseconds.
            .addScanType(ScanType.SINGLE) // ScanType.SINGLE: scans for Bluetooth LE devices.
            .addScanType(ScanType.SIG_MESH) // ScanType.SINGLE: scans for Bluetooth LE devices.
            .addScanType(ScanType.MESH) // ScanType.SINGLE: scans for Bluetooth LE devices.
            // .addScanType(ScanType.SIG_MESH): scans for other types of devices.

            .build()


// Starts scanning.


// Starts scanning.
        ThingHomeSdk.getBleOperator().startLeScan(scanSetting, object : BleScanResponse {
            override fun onResult(bean: ScanDeviceBean?) {
                ThingHomeSdk.getActivatorInstance().getActivatorToken(
                    172730637,
                    object : IThingActivatorGetToken {
                        override fun onSuccess(token: String?) {
                            Toast.makeText(
                                application.applicationContext,
                                "ENCONTRADO",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            activate(bean!!, token, 172730637)
                        }

                        override fun onFailure(s: String?, s1: String?) {
                            System.out.println(s1)
                            Toast.makeText(
                                application.applicationContext,
                                "ERROR",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    })

            }
        })
    }

    private fun getDeviceInfo(bean: ScanDeviceBean) {
        ThingHomeSdk.getActivatorInstance().getActivatorDeviceInfo(
            bean.productId,
            bean.uuid,
            bean.mac,
            object : IThingDataCallback<ConfigProductInfoBean> {
                override fun onSuccess(result: ConfigProductInfoBean?) {
                    println(result)
                    TODO("Not yet implemented")
                }

                override fun onError(errorCode: String?, errorMessage: String?) {
                    println(errorMessage)
                    TODO("Not yet implemented")
                }

            }
        )
    }

    fun list() {
        ThingHomeSdk.newHomeInstance(172730637).getHomeDetail(object : IThingHomeResultCallback {
            override fun onSuccess(bean: HomeBean?) {
                val deviceList: List<DeviceBean> = bean!!.deviceList
                val mDevice= ThingHomeSdk.newDeviceInstance(deviceList[1].devId)
                mDevice.publishDps("{\"01\": true}", object : IResultCallback {
                    override fun onError(code: String?, error: String?) {
                        Toast.makeText(
                            application.applicationContext,
                            "Failed to switch on the light.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onSuccess() {
                        Toast.makeText(
                            application.applicationContext,
                            "The light is switched on successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                //                var sensors = ArrayList<Sensor>()
//                deviceList.forEach {
//                    var dps = ThingHomeSdk.getDataInstance().getDps(deviceList[0].devId)
//                    var temperature = 0.0
//                    var temperatureDps = dps!!["1"]
//                    if (temperatureDps is String) {
//                        temperature = temperatureDps.toDouble()
//                    }
//                    var state = ThingHomeSdk.getDataInstance()
//                        .getDeviceBean(deviceList[0].devId)!!.isCloudOnline
//
//                    sensors.add(Sensor(it.getName(),temperature, state))
//                }
//                // Get `deviceBean`.
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                if (errorCode == "USER_SESSION_LOSS")
                    login()
                println(errorMsg)

                // do something
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
        multiModeActivatorBean.ssid = "Casa"; // The SSID of the target Wi-Fi network.
        multiModeActivatorBean.pwd = "salinasaviles9800";
        multiModeActivatorBean.token = token; // The pairing token.
        multiModeActivatorBean.homeId = homeId; // The value of `homeId` for the current home.
        multiModeActivatorBean.timeout = 100000; // The timeout value.
        ThingHomeSdk.getActivator().newMultiModeActivator()
            .startActivator(multiModeActivatorBean, object : IMultiModeActivatorListener {
                override fun onSuccess(deviceBean: DeviceBean?) {
                    println(deviceBean)
                    Toast.makeText(
                        application.applicationContext,
                        "OK",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    // The device is paired.
                }

                override fun onFailure(code: Int, msg: String, handle: Any) {
                    Toast.makeText(
                        application.applicationContext,
                        "ERROR",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    println(msg)

                    // Failed to pair the device.
                }

            })
    }

//    fun createHome() {
//        var rooms = mutableListOf(
//            "salon",
//            "cocina"
//        )
//        ThingHomeSdk.getHomeManagerInstance()
//            .createHome("Casa", 0.0, 0.0, "Gijon", rooms, object : IThingHomeResultCallback {
//                override fun onSuccess(bean: HomeBean?) {
//                    System.out.println(bean)
//                    pairDevice()
//                    // do something
//                }
//
//                override fun onError(errorCode: String?, errorMsg: String?) {
//                    System.out.println(errorMsg)
//
//                    // do something
//                }
//            })
//    }

    //    172730637
//    fun pairDevice() {
//
//        ThingHomeSdk.getHomeManagerInstance().queryHomeList(object : IThingGetHomeListCallback {
//            override fun onSuccess(homeBeans: List<HomeBean?>?) {
//                if (homeBeans !== null && homeBeans.isNotEmpty())
//                    ThingHomeSdk.getActivatorInstance().getActivatorToken(
//                        homeBeans.get(0)!!.homeId,
//                        object : IThingActivatorGetToken {
//                            @RequiresApi(Build.VERSION_CODES.Q)
//                            override fun onSuccess(token: String?) {
//                                pair(token)
//                            }
//
//                            override fun onFailure(s: String?, s1: String?) {
//                                System.out.println(s1)
//
//                            }
//                        })
//            }
//
//            override fun onError(errorCode: String?, error: String?) {
//                // do something
//            }
//        })
//
//    }

//    @RequiresApi(Build.VERSION_CODES.Q)
//    fun pair(token: String?) {
//
//        val scanSetting = LeScanSetting.Builder()
//            .setTimeout(60000) // The duration of the scanning. Unit: milliseconds.
//            .addScanType(ScanType.SINGLE) // ScanType.SINGLE: scans for Bluetooth LE devices.
//            // .addScanType(ScanType.SIG_MESH): scans for other types of devices.
//            .build()
//
//// Starts scanning.
//
//// Starts scanning.
//        ThingHomeSdk.getBleOperator().startLeScan(scanSetting, object : BleScanResponse {
//            override fun onResult(bean: ScanDeviceBean?) {
//                activate(bean)
//            }
//
//        })
//        val cm: ConnectivityManager = application.applicationContext.getSystemService(ConnectivityManager::class.java)
//        val n = cm.activeNetwork
//        val netCaps = cm.getNetworkCapabilities(n)
//        val info = netCaps!!.transportInfo as WifiInfo
//        val ssid: String = info.ssid
//
//        var builder: ActivatorBuilder = ActivatorBuilder()
//            .setSsid(ssid)
//            .setPassword("")
//            .setContext(application.applicationContext)
//            .setActivatorModel(ActivatorModelEnum.THING_EZ)
//            .setTimeOut(100)
//            .setToken(token)
//            .setListener(object:IThingSmartActivatorListener{
//
//                override fun onError(errorCode: String?, errorMsg: String?) {
//                    System.out.println(errorMsg)
//
//                }
//
//                override fun onActiveSuccess(devResp: DeviceBean?) {
//                    System.out.println(devResp)
//
//                }
//
//                override fun onStep(step: String?, data: Any?) {
//                    System.out.println(step)
//
//                }
//            }
//            )


//    }

}