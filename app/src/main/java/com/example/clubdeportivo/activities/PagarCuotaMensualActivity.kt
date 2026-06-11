package com.example.clubdeportivo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R
import com.example.clubdeportivo.adapters.CuotaPendienteAdapter
import com.example.clubdeportivo.database.dao.ClienteDao
import com.example.clubdeportivo.database.dao.CuotaMensualDao


class PagarCuotaMensualActivity : AppCompatActivity() {

    private lateinit var txtNombreApellido: TextView
    private lateinit var txtModoPago: AutoCompleteTextView
    private lateinit var txtMontoPagar: TextView
    private lateinit var rvCuotas: RecyclerView
    private lateinit var mensajeError: TextView
    private lateinit var btnVolver: ImageView
    private lateinit var btnCancelar: LinearLayout
    private lateinit var btnPagarCuota: Button


    private var idCliente = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar_cuota_mensual)

        inicializarVistas()

        configurarModoPago()

        idCliente = intent.getIntExtra(
            "idCliente",
            -1
        )

        if (idCliente != -1) {
            cargarDatos()
        }


        // BOTON VOLVER
        btnVolver.setOnClickListener {
            finish()
        }

        // BOTON CANCELAR
        btnCancelar.setOnClickListener {
            finish()
        }

        // BOTON PAGAR CUOTA
        btnPagarCuota.setOnClickListener {
            pagarCuotaMensual()
        }

    }

    private fun inicializarVistas() {

        txtNombreApellido = findViewById(R.id.nombreSocio)
        txtModoPago = findViewById(R.id.modo_pago)
        txtMontoPagar = findViewById(R.id.txtTotalPagar)
        mensajeError = findViewById(R.id.mensajeError)
        rvCuotas = findViewById(R.id.rvCuotas)
        btnVolver = findViewById(R.id.btnVolver)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnPagarCuota = findViewById(R.id.btnPagarCuota)
    }

    private fun cargarDatos() {

        val clienteDao = ClienteDao(this)

        val cuotaDao = CuotaMensualDao(this)

        val cliente = clienteDao.obtenerPorId(idCliente)

        if (cliente != null) {
            txtNombreApellido.text = "${cliente.nombre} ${cliente.apellido}"
        }

        val cuotasPendientes = cuotaDao.obtenerPendientes(idCliente)

        rvCuotas.layoutManager = LinearLayoutManager(this)

        rvCuotas.adapter = CuotaPendienteAdapter(cuotasPendientes)

        val total =  cuotasPendientes.sumOf { it.monto }

        txtMontoPagar.text = "$ $total"
    }

    private fun configurarModoPago() {

        val opciones = listOf(
            "EFECTIVO",
            "TARJETA"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            opciones
        )

        txtModoPago.setAdapter(adapter)

        txtModoPago.setOnClickListener {
            txtModoPago.showDropDown()
        }

        txtModoPago.setText(opciones[0], false)
    }

    private fun pagarCuotaMensual(){

        val cuotaDao = CuotaMensualDao(this)

        val modoPago = txtModoPago.text.toString()

        val filas = cuotaDao.pagarPendientes(idCliente, modoPago)

        if (filas > 0) {

            val vista = layoutInflater.inflate(R.layout.dialog_template, null)

            val txtMensaje = vista.findViewById<TextView>(R.id.textDialog)

            txtMensaje.text = "Pago realizado correctamente"

            val btnAceptar = vista.findViewById<Button>(R.id.btnAceptar)

            val dialog = AlertDialog.Builder(this)
                .setView(vista)
                .create()

            dialog.show()

            btnAceptar.setOnClickListener {

                dialog.dismiss()

                val intent = Intent(
                    this,
                    CarnetDigitalActivity::class.java
                )

                intent.putExtra(
                    "idCliente",
                    idCliente
                )

                startActivity(intent)

                finish()
            }

        } else {
            mostrarError(
                mensajeError,
                "Error al pagar las cuotas"
            )
            return
        }

    }


    private fun mostrarError(textView: TextView, mensaje: String) {
        textView.text = mensaje
        textView.visibility = View.VISIBLE

        textView.postDelayed({
            textView.visibility = View.INVISIBLE
        }, 3000)
    }
}