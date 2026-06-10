package com.example.clubdeportivo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R
import com.example.clubdeportivo.ui.model.MorosoUi

class MorosoAdapter(
    private val lista: List<MorosoUi>
) : RecyclerView.Adapter<MorosoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val numero = view.findViewById<TextView>(R.id.txtNumero)
        val nombre = view.findViewById<TextView>(R.id.txtNombreCompleto)
        val estado = view.findViewById<TextView>(R.id.txtEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_morosos, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = lista[position]

        holder.numero.text = (position + 1).toString()
        holder.nombre.text = "${item.nombre} ${item.apellido}"
        holder.estado.text = item.estado
    }

    override fun getItemCount(): Int = lista.size
}