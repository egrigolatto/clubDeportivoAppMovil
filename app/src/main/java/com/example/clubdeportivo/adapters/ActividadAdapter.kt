package com.example.clubdeportivo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R
import com.example.clubdeportivo.ui.model.ActividadUi


class ActividadAdapter(
    private val lista: List<ActividadUi>,
    private val onSelectionChanged: () -> Unit
) : RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nombre = view.findViewById<TextView>(R.id.nombreActividad)
        val precio = view.findViewById<TextView>(R.id.precioActividad)
        val check = view.findViewById<CheckBox>(R.id.checkActividad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val actividad = lista[position]

        holder.nombre.text = actividad.nombre
        holder.precio.text = "$ ${actividad.monto}"

        holder.check.setOnCheckedChangeListener(null)
        holder.check.isChecked = actividad.seleccionada

        holder.check.setOnCheckedChangeListener { _, isChecked ->

            actividad.seleccionada = isChecked
            onSelectionChanged()
        }
    }

    override fun getItemCount(): Int = lista.size
}