package com.tfm.control.climatizacion

import android.app.Application
import android.widget.Button
import com.google.android.material.tabs.TabLayout
import com.thingclips.smart.home.sdk.ThingHomeSdk

class MainApp : Application() {
    private lateinit var tabLayout:TabLayout;
    override fun onCreate() {
        super.onCreate()
        ThingHomeSdk.setDebugMode(true);
        ThingHomeSdk.init(this);
    }


}