package com.tfm.control.climatizacion

import android.app.Application
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.thingclips.smart.android.user.api.IWhiteListCallback
import com.thingclips.smart.android.user.bean.WhiteList
import com.thingclips.smart.home.sdk.ThingHomeSdk


class MainApp : Application() {
    private lateinit var tabLayout:TabLayout;
    override fun onCreate() {
        super.onCreate()

    }
}