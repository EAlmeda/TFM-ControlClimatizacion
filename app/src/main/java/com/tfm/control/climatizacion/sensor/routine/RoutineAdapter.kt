package com.tfm.control.climatizacion.sensor.routine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfm.control.climatizacion.R
import com.tfm.control.climatizacion.models.Routine

class RoutineAdapter(var routines: List<Routine>) :
    RecyclerView.Adapter<RoutineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_routine, parent, false)
        val routineViewHolder = RoutineViewHolder(view)
        return routineViewHolder
    }

    override fun getItemCount()= routines.size

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        holder.render(routines[position])
    }

}