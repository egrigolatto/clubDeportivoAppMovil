package com.example.clubdeportivo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.clubdeportivo.R
import com.example.clubdeportivo.database.dao.ClienteDao
import com.example.clubdeportivo.models.Cliente


class RegistroSociosActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtDni: EditText
    private lateinit var tipoDoc: AutoCompleteTextView
    private lateinit var txtEmail: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtModoPago: AutoCompleteTextView
    private lateinit var txtMontoPagar: TextView
    private lateinit var chkAptoFisico: CheckBox
    private lateinit var mensajeError: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_socios)

        inicializarVistas()
        configurarTipoDocumento()
        configurarModoPago()

        // BOTON VOLVER
        val btnVolver = findViewById<ImageView>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }
        // BOTON CANCELAR
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        btnCancelar.setOnClickListener {
            finish()
        }
        // BOTON REGISTRAR SOCIO
        val btnRegistrarSocio = findViewById<Button>(R.id.btnRegistrarSocio)

        btnRegistrarSocio.setOnClickListener {

            val nombre = txtNombre.text.toString().trim()
            val apellido = txtApellido.text.toString().trim()
            val dni = txtDni.text.toString().trim()
            val email = txtEmail.text.toString().trim()
            val telefono = txtTelefono.text.toString().trim()
            val aptoFisico = chkAptoFisico.isChecked

            val tipoDocumento = 1 //despues crear la funcion para traerlo de la db

            if (
                nombre.isEmpty() ||
                apellido.isEmpty() ||
                dni.isEmpty() ||
                email.isEmpty()  ||
                telefono.isEmpty()
            ) {

                mostrarError(
                    mensajeError,
                    "Complete todos los campos obligatorios"
                )

                return@setOnClickListener
            }

            if (!aptoFisico) {

                mostrarError(
                    mensajeError,
                    "Debe presentar apto físico"
                )

                return@setOnClickListener
            }

            val clienteDao = ClienteDao(this)

            if (
                clienteDao.existeDocumento(
                    tipoDocumento,
                    dni
                )
            ) {

                mostrarError(
                    mensajeError,
                    "Ya existe un cliente con ese documento"
                )

                return@setOnClickListener
            }

            val cliente = Cliente(
                nombre = nombre,
                apellido = apellido,
                tipoDocumento = tipoDocumento,
                numeroDocumento = dni,
                email = email,
                telefono = telefono,
                esSocio = true,
                aptoFisico = aptoFisico,
                estado = "activo"
            )

            val resultado = clienteDao.insertar(cliente)

            if (resultado > 0) {

                val vista = layoutInflater.inflate(R.layout.dialog_registro, null)

                val txtMensajeRegistro = vista.findViewById<TextView>(R.id.txtMensajeRegistro)

                txtMensajeRegistro.text =
                    """
                       Socio registrado correctamente
                       Nombre: $nombre
                       Apellido: $apellido
                       DNI: $dni
                       """.trimIndent()

                val btnAceptar = vista.findViewById<Button>(R.id.btnAceptar)

                val dialog = AlertDialog.Builder(this)
                    .setView(vista)
                    .create()

                dialog.show()

                btnAceptar.setOnClickListener {

                    dialog.dismiss()

                    txtNombre.text.clear()
                    txtApellido.text.clear()
                    txtDni.text.clear()
                    txtEmail.text.clear()
                    txtTelefono.text.clear()

                    chkAptoFisico.isChecked = false


                }

            } else {
                mostrarError(
                    mensajeError,
                    "Error al crear el Socio"
                )
            }

        }


    }

    private fun inicializarVistas() {

        txtNombre = findViewById(R.id.nombre)
        txtApellido = findViewById(R.id.apellido)
        txtDni = findViewById(R.id.dni)
        tipoDoc = findViewById(R.id.tipoDocumento)
        txtEmail = findViewById(R.id.email)
        txtTelefono = findViewById(R.id.telefono)
        chkAptoFisico = findViewById(R.id.aptoFisico)
        txtModoPago = findViewById(R.id.modo_pago)
        txtMontoPagar = findViewById(R.id.monto_pagar)
        mensajeError = findViewById(R.id.mensajeError)
    }

    private fun configurarTipoDocumento() {

        val opciones = listOf(
            "DNI",
            "Pasaporte"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            opciones
        )

        tipoDoc.setAdapter(adapter)

        tipoDoc.setOnClickListener {
            tipoDoc.showDropDown()
        }

        tipoDoc.setText(opciones[0], false)
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
    private fun mostrarError(textView: TextView, mensaje: String) {
        textView.text = mensaje
        textView.visibility = View.VISIBLE

        textView.postDelayed({
            textView.visibility = View.INVISIBLE
        }, 3000)
    }
}