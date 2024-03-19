package com.tfm.control.climatizacion.sensor.routine

import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Condition
import com.tfm.control.climatizacion.models.Routine

class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val txtName: TextView = view.findViewById(R.id.routine_name)
    private val txtTemperature: TextView = view.findViewById(R.id.routine_temperature)
    private val switch: SwitchCompat = view.findViewById(R.id.routine_active)
    private val btnDelete: ImageButton = view.findViewById(R.id.routine_delete)

    fun render(
        routine: Routine,
        notifyDelete: (Int) -> Unit,
        notifyStatus: (Int, Boolean) -> Unit
    ) {
        txtName.text = routine.name
        txtTemperature.text =
            "${getConditionName(routine.condition)} ${"%.1f".format(routine.temperatureCondition)} ºC"
        switch.isChecked = routine.isActivated

        switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                notifyStatus(routine.id, isChecked)
            }
        })
        btnDelete.setOnClickListener {
            notifyDelete(routine.id)
        }
    }

    private fun getConditionName(condition: Condition): String {
        when (condition) {
            Condition.LESS -> return "Menor que"
            Condition.GREATER -> return "Mayor que"
            else -> return "Igual a"
        }
    }

}