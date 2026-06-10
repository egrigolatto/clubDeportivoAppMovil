package com.example.clubdeportivo.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R
import com.example.clubdeportivo.models.CuotaMensual
import com.example.clubdeportivo.utils.Config

class CuotaPendienteAdapter(
    private val cuotas: List<CuotaMensual>
) : RecyclerView.Adapter<CuotaPendienteAdapter.ViewHolder>() {

    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val txtPeriodo: TextView =
            view.findViewById(R.id.txtPeriodo)

        val txtFechaVencimiento: TextView =
            view.findViewById(R.id.txtFechaVencimiento)

        val txtMonto: TextView =
            view.findViewById(R.id.txtMonto)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_cuota_pendiente,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val cuota = cuotas[position]

        holder.txtPeriodo.text =
            cuota.periodo

        holder.txtFechaVencimiento.text =
            cuota.fechaVencimiento

        holder.txtMonto.text =
            "$ ${Config.MONTO_CUOTA_MENSUAL}"
    }

    override fun getItemCount(): Int {
        return cuotas.size
    }
}