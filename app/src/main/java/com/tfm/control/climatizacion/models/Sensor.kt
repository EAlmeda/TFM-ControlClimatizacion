package com.tfm.control.climatizacion.models

import java.io.Serializable

class Sensor(var id:String, var name: String, var temperature: Double, var isConnected: Boolean) : Serializable {
}