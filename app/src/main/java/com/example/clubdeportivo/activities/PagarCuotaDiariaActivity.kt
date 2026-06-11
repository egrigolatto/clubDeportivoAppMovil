package com.example.clubdeportivo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R
import com.example.clubdeportivo.adapters.ActividadAdapter
import com.example.clubdeportivo.database.dao.ActividadDao
import com.example.clubdeportivo.database.dao.ClienteDao
import com.example.clubdeportivo.database.dao.CuotaDiariaDao
import com.example.clubdeportivo.ui.model.ActividadUi

class PagarCuotaDiariaActivity : AppCompatActivity() {

    private lateinit var txtNombreApellido: TextView
    private lateinit var txtModoPago: AutoCompleteTextView
    private lateinit var txtMontoPagar: TextView
    private lateinit var rvActividades: RecyclerView
    private lateinit var mensajeError: TextView
    private lateinit var listaUi: MutableList<ActividadUi>
    private lateinit var btnVolver: ImageView
    private lateinit var btnCancelar: LinearLayout
    private lateinit var btnPagarCuota: Button


    private var idCliente = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar_cuota_diaria)

        inicializarVistas()
        configurarModoPago()
        cargarActividades()

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
            pagarCuotaDiaria()
        }
    }

    private fun inicializarVistas() {
        txtNombreApellido = findViewById(R.id.nombreSocio)
        txtModoPago = findViewById(R.id.modo_pago)
        txtMontoPagar = findViewById(R.id.txtTotalPagar)
        mensajeError = findViewById(R.id.mensajeError)
        rvActividades = findViewById(R.id.rvActividades)
        btnVolver = findViewById(R.id.btnVolver)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnPagarCuota = findViewById(R.id.btnPagarCuota)
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

    private fun cargarActividades() {

        val actividadDao = ActividadDao(this)

        val actividadesDB = actividadDao.obtenerTodas()

        listaUi = actividadesDB.map {
            ActividadUi(
                idActividad = it.idActividad,
                nombre = it.nombre,
                monto = it.monto
            )
        }.toMutableList()

        val adapter = ActividadAdapter(listaUi) {

            val total = listaUi
                .filter { it.seleccionada }
                .sumOf { it.monto }

            txtMontoPagar.text = "$ $total"
        }

        rvActividades.layoutManager = LinearLayoutManager(this)

        rvActividades.adapter = adapter
    }

    private fun cargarDatos() {

        val clienteDao = ClienteDao(this)

        val cliente = clienteDao.obtenerPorId(idCliente)

        if (cliente != null) {
            txtNombreApellido.text = "${cliente.nombre} ${cliente.apellido}"
        }
    }

    private fun pagarCuotaDiaria() {

        val modoPago = txtModoPago.text.toString()

        val seleccionadas = listaUi.filter { it.seleccionada }

        if (seleccionadas.isEmpty()) {
            mostrarError(mensajeError, "Seleccione al menos una actividad")
            return
        }

        val cuotaDao = CuotaDiariaDao(this)

        var exitos = 0
        var duplicadas = 0

        seleccionadas.forEach { actividad ->

            val resultado = cuotaDao.crearCuotaDiaria(
                idCliente = idCliente,
                idActividad = actividad.idActividad,
                modoPago = modoPago
            )

            when {
                resultado > 0 -> exitos++
                resultado == -1L -> duplicadas++
            }
        }

        if (exitos > 0) {

            val vista = layoutInflater.inflate(R.layout.dialog_template, null)

            val mensaje = vista.findViewById<TextView>(R.id.textDialog)
            val btnAceptar = vista.findViewById<Button>(R.id.btnAceptar)

            mensaje.text =
                if (duplicadas > 0) {
                    """
            Se registraron $exitos actividades.

            $duplicadas actividades ya habían sido abonadas hoy.
            """.trimIndent()
                } else {
                    "Pago realizado correctamente"
                }

            val dialog = AlertDialog.Builder(this)
                .setView(vista)
                .create()

            dialog.show()

            btnAceptar.setOnClickListener {
                dialog.dismiss()
                finish()
            }

        } else {

            mostrarError(
                mensajeError,
                "Todas las actividades seleccionadas ya fueron abonadas hoy"
            )
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