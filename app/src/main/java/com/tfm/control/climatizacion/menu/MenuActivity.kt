package com.tfm.control.climatizacion.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.compose.animation.core.animateDpAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.plug.PlugsFragment
import com.tfm.control.climatizacion.sensor.SensorsFragment

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val fragment1 = SensorsFragment()
        val fragment2 = PlugsFragment()
        makeCurrentFragment(fragment1)
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            when(it.itemId){
                R.id.page_plugs -> makeCurrentFragment(fragment2)
                R.id.page_sensors -> makeCurrentFragment(fragment1)
            }
            true
        }

    }
    private fun makeCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper,fragment)
            commit()
        }
    }
}