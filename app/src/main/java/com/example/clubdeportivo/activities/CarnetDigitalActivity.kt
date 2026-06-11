package com.example.clubdeportivo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.R
import com.example.clubdeportivo.database.dao.ClienteDao
import com.example.clubdeportivo.database.dao.CuotaMensualDao


class CarnetDigitalActivity : AppCompatActivity() {

    private lateinit var txtNombreApellido: TextView
    private lateinit var txtDocumento: TextView
    private lateinit var txtPeriodo: TextView
    private lateinit var txtFechaVencimiento: TextView
    private lateinit var btnVolver: ImageView
    private lateinit var btnCompartir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet_digital)

        inicializarVistas()

        val idCliente = intent.getIntExtra(
            "idCliente",
            -1
        )

        if (idCliente != -1) {
            cargarDatosCarnet(idCliente)
        }

        // BOTON VOLVER
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

        // BOTON COMPARTIR
        btnCompartir.setOnClickListener {

            val vista = layoutInflater.inflate(R.layout.dialog_template, null)

            val txtMensaje = vista.findViewById<TextView>(R.id.textDialog)

            txtMensaje.text = "Carnet digital enviado al Cliente"

            val btnAceptar = vista.findViewById<Button>(R.id.btnAceptar)

            val dialog = AlertDialog.Builder(this)
                .setView(vista)
                .create()

            dialog.show()

            btnAceptar.setOnClickListener {

                dialog.dismiss()

                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)

                finish()
            }
        }

    }

    private fun inicializarVistas() {

        txtNombreApellido = findViewById(R.id.nombreSocio)
        txtDocumento = findViewById(R.id.numeroDocumento)
        txtPeriodo = findViewById(R.id.periodo)
        txtFechaVencimiento = findViewById(R.id.fechaVencimiento)
        btnVolver = findViewById(R.id.btnVolver)
        btnCompartir = findViewById(R.id.btnCompartir)
    }

    private fun cargarDatosCarnet(
        idCliente: Int
    ) {

        val clienteDao = ClienteDao(this)

        val cuotaDao = CuotaMensualDao(this)

        val cliente =  clienteDao.obtenerPorId(idCliente)

        if (cliente != null) {

            txtNombreApellido.text =
                "${cliente.nombre} ${cliente.apellido}"

            txtDocumento.text =  cliente.numeroDocumento

        }

        val cuota =  cuotaDao.obtenerUltimaCuotaVigente(idCliente)

        if (cuota != null) {

            txtPeriodo.text = cuota.periodo

            txtFechaVencimiento.text =
                cuota.fechaVencimiento
        }
    }
}