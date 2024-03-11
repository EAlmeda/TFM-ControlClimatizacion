package com.tfm.control.climatizacion.models

import java.io.Serializable

class Routine(
    var name: String,
    var sensorId: String,
    var plugId: String,
    var isActivated: Boolean,
    var temperatureCondition : Double,
    var condition: Condition,
    var plugResult: Boolean
) : Serializable {
}