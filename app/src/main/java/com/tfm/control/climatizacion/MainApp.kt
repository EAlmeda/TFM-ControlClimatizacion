package com.tfm.control.climatizacion

import android.app.Application
import com.thingclips.smart.home.sdk.ThingHomeSdk


class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ThingHomeSdk.setDebugMode(true);
        ThingHomeSdk.init(this);

    }
}