package com.example.clubdeportivo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.clubdeportivo.R
import com.example.clubdeportivo.database.dao.ClienteDao
import com.example.clubdeportivo.database.dao.CuotaMensualDao

class PagarCuotaActivity : AppCompatActivity() {

    private lateinit var txtDocumento: EditText
    private lateinit var mensajeError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar_cuota)

        inicializarVistas()

        // BOTON VOLVER
        val btnVolver = findViewById<ImageView>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }

        // BOTON CANCELAR
        val btnCancelar = findViewById<LinearLayout>(R.id.btnCancelar)
        btnCancelar.setOnClickListener {
            finish()
        }

        // BOTON CONFIRMAR
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)

        btnConfirmar.setOnClickListener {

            val documento = txtDocumento.text.toString().trim()

            if (documento.isEmpty()) {
                mostrarError(
                    mensajeError,
                    "Ingrese un documento"
                )
                return@setOnClickListener
            }

            val clienteDao = ClienteDao(this)

            val cliente = clienteDao.obtenerPorDocumento(documento)

            if (cliente == null) {
                mostrarDialogo(
                    "El cliente no se encuentra registrado",
                    2
                )
                return@setOnClickListener
            }

            val cuotaMensualDao = CuotaMensualDao(this)

            if (cliente.esSocio) {
                // Genera automáticamente las cuotas faltantes
                cuotaMensualDao.actualizarCuotasPendientes(
                    cliente.idCliente
                )

                val cuotasPendientes =
                    cuotaMensualDao.obtenerPendientes(
                        cliente.idCliente
                    )

                if (cuotasPendientes.isEmpty()) {

                    mostrarDialogo(
                        "No registra deuda",
                        1
                    )

                    return@setOnClickListener
                }

                val intent = Intent(
                    this,
                    PagarCuotaMensualActivity::class.java
                )

                intent.putExtra(
                    "idCliente",
                    cliente.idCliente
                )

                startActivity(intent)

            } else {

                val intent = Intent(
                    this,
                    PagarCuotaDiariaActivity::class.java
                )

                intent.putExtra(
                    "idCliente",
                    cliente.idCliente
                )

                startActivity(intent)
            }
        }


    }

    private fun inicializarVistas() {
        txtDocumento = findViewById(R.id.documentoCliente)
        mensajeError = findViewById(R.id.mensajeError)
    }

    private fun mostrarDialogo(
        mensajeTexto: String,
        tipoDialogo: Int
    ) {

        val vista = if (tipoDialogo == 1) {
            layoutInflater.inflate(
                R.layout.dialog_template,
                null
            )
        } else {
            layoutInflater.inflate(
                R.layout.dialog_error,
                null
            )
        }

        val mensaje = vista.findViewById<TextView>(
            R.id.textDialog
        )

        val btnAceptar = vista.findViewById<Button>(
            R.id.btnAceptar
        )

        mensaje.text = mensajeTexto

        val dialog = AlertDialog.Builder(this)
            .setView(vista)
            .create()

        dialog.show()

        btnAceptar.setOnClickListener {
            dialog.dismiss()
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