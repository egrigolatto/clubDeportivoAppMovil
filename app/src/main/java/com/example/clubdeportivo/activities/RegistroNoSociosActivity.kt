package com.example.clubdeportivo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.R
import com.example.clubdeportivo.database.dao.ClienteDao
import com.example.clubdeportivo.database.dao.TipoDocumentoDao
import com.example.clubdeportivo.models.Cliente
import android.view.View

class RegistroNoSociosActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtDocumento: EditText
    private lateinit var tipoDocumento: AutoCompleteTextView
    private lateinit var txtEmail: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var chkAptoFisico: CheckBox
    private lateinit var mensajeError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_no_socios)

        inicializarVistas()
        configurarTipoDocumento()

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
        // BOTON REGISTRAR NO SOCIO
        val btnRegistrarNoSocio = findViewById<Button>(R.id.btnRegistrarNoSocio)

        btnRegistrarNoSocio.setOnClickListener {

            val tipoDocumentoDao = TipoDocumentoDao(this)
            val tipoDocumentoSeleccionado =
                tipoDocumentoDao.obtenerPorNombre(
                    tipoDocumento.text.toString()
                )
            if (tipoDocumentoSeleccionado == null) {
                mostrarError(
                    mensajeError,
                    "Seleccione un tipo de documento válido"
                )
                return@setOnClickListener
            }

            val nombre = txtNombre.text.toString().trim()
            val apellido = txtApellido.text.toString().trim()
            val documento = txtDocumento.text.toString().trim()
            val email = txtEmail.text.toString().trim()
            val telefono = txtTelefono.text.toString().trim()
            val aptoFisico = chkAptoFisico.isChecked
            val idTipoDocumento = tipoDocumentoSeleccionado.idTipoDocumento

            if (
                nombre.isEmpty() ||
                apellido.isEmpty() ||
                documento.isEmpty() ||
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
                    idTipoDocumento,
                    documento
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
                tipoDocumento = idTipoDocumento,
                numeroDocumento = documento,
                email = email,
                telefono = telefono,
                esSocio = false,
                aptoFisico = aptoFisico,
                estado = "activo"
            )

            val resultado = clienteDao.insertar(cliente)

            if (resultado > 0) {

                val vista = layoutInflater.inflate(R.layout.dialog_registro, null)

                val txtMensajeRegistro = vista.findViewById<TextView>(R.id.txtMensajeRegistro)

                txtMensajeRegistro.text =
                    """
                       No Socio registrado correctamente
                       Nombre: $nombre
                       Apellido: $apellido
                       DNI: $documento
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
                    txtDocumento.text.clear()
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
        txtDocumento = findViewById(R.id.documento)
        tipoDocumento = findViewById(R.id.tipoDocumento)
        txtEmail = findViewById(R.id.email)
        txtTelefono = findViewById(R.id.telefono)
        chkAptoFisico = findViewById(R.id.aptoFisico)
        mensajeError = findViewById(R.id.mensajeError)
    }

    private fun configurarTipoDocumento() {

        val tipoDocumentoDao = TipoDocumentoDao(this)

        val tiposDocumento = tipoDocumentoDao.obtenerTodos()

        val nombres = tiposDocumento.map { it.nombre }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombres
        )

        tipoDocumento.setAdapter(adapter)

        tipoDocumento.setOnClickListener {
            tipoDocumento.showDropDown()
        }

        if (nombres.isNotEmpty()) {
            tipoDocumento.setText(nombres[0], false)
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