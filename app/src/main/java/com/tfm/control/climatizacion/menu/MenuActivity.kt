package com.tfm.control.climatizacion.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.plug.PlugsFragment
import com.tfm.control.climatizacion.sensor.SensorsFragment
import com.tfm.control.climatizacion.tuya.TuyaManager
import com.thingclips.smart.home.sdk.ThingHomeSdk


class MenuActivity : AppCompatActivity() {
    public lateinit var tuyaManager: TuyaManager

    lateinit var btnAdd: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val fragment1 = SensorsFragment()
        val fragment2 = PlugsFragment()
        makeCurrentFragment(fragment1)
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page_plugs -> makeCurrentFragment(fragment2)
                R.id.page_sensors -> makeCurrentFragment(fragment1)
            }
            true
        }

        tuyaManager = TuyaManager.getInstance(application)
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }

    override fun onDestroy() {
        ThingHomeSdk.onDestroy()
        super.onDestroy()
    }

}