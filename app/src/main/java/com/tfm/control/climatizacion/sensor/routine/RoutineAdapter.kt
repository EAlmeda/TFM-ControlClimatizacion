package com.tfm.control.climatizacion.sensor.routine

import android.icu.text.RelativeDateTimeFormatter.RelativeUnit
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.DatabaseHelper
import com.tfm.control.climatizacion.models.Routine
import com.thingclips.smart.sdk.bean.DeviceBean

class RoutineAdapter(var routines: ArrayList<Routine>, var checkEmpty: (Int) -> Unit) :
    RecyclerView.Adapter<RoutineViewHolder>() {

    private lateinit var db: DatabaseHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_routine, parent, false)
        db = DatabaseHelper(view.context)
        return RoutineViewHolder(view)
    }

    fun setData(list: ArrayList<Routine>) {
        routines = list
    }

    override fun getItemCount() = routines.size

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        holder.render(routines[position], this::delete, this::status)
    }

    private fun delete(id: Int) {
        db.delete(id)
        routines.removeIf { r -> r.id == id }
        notifyDataSetChanged()
        checkEmpty(routines.size)
    }

    private fun status(id: Int, status: Boolean) {
        db.changeStatus(id, status)
    }

}