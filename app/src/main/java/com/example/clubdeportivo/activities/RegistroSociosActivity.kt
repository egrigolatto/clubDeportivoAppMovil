package com.example.clubdeportivo.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
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
import com.example.clubdeportivo.database.dao.CuotaMensualDao
import com.example.clubdeportivo.database.dao.TipoDocumentoDao
import com.example.clubdeportivo.models.Cliente
import com.example.clubdeportivo.utils.Config


class RegistroSociosActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtDocumento: EditText
    private lateinit var tipoDocumento: AutoCompleteTextView
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

        txtMontoPagar.text = "$ ${Config.MONTO_CUOTA_MENSUAL}"

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
                esSocio = true,
                aptoFisico = aptoFisico,
                estado = "activo"
            )

            val resultado = clienteDao.insertar(cliente)

            if (resultado > 0) {

                val idCliente = resultado.toInt()

                val cuotaDao = CuotaMensualDao(this)

                val cuota =cuotaDao.crearPrimeraCuota(
                    idCliente,
                    txtModoPago.text.toString()
                )

                if (cuota == null) {
                    mostrarError(
                        mensajeError,
                        "Error al crear la cuota mensual"
                    )
                    return@setOnClickListener
                }

                val vista = layoutInflater.inflate(R.layout.dialog_registro, null)

                val txtMensajeRegistro = vista.findViewById<TextView>(R.id.txtMensajeRegistro)

                txtMensajeRegistro.text =
                    """
                       Socio registrado correctamente
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
        txtModoPago = findViewById(R.id.modo_pago)
        txtMontoPagar = findViewById(R.id.monto_pagar)
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