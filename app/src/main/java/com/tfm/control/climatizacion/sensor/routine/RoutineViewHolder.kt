package com.tfm.control.climatizacion.sensor.routine

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Routine

class RoutineViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val txtName: TextView = view.findViewById(R.id.routine_name)
//    private val txtTemperature: TextView = view.findViewById(R.id.routine_connected)

    fun render(routine: Routine) {
        txtName.text = routine.name
    }
}