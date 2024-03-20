package com.tfm.control.climatizacion.models

import java.io.Serializable

class Plug(var id: String, var name: String, var state: Boolean, var isConnected: Boolean) :
    Serializable {

}