package com.example.clubdeportivo.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.R
import com.example.clubdeportivo.database.dao.UsuarioDao
import com.example.clubdeportivo.models.Usuario

class RegistrarUsuarioActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtNumeroDocumento: EditText
    private lateinit var tipoDocumento: AutoCompleteTextView
    private lateinit var tipoRol: AutoCompleteTextView
    private lateinit var txtPassword1: EditText
    private lateinit var txtPassword2: EditText
    private lateinit var mensajeError: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        inicializarVistas()

        configurarTipoDocumento()

        configurarRoles()

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


        // BOTON REGISTRAR USUARIO
        val btnRegistrarUsuario = findViewById<Button>(R.id.btnRegistrarUsuario)

        btnRegistrarUsuario.setOnClickListener {

            val nombre = txtNombre.text.toString().trim()

            val apellido = txtApellido.text.toString().trim()

            val tipoDocumento = 1

            val numeroDocumento = txtNumeroDocumento.text.toString().trim()

            val password1 = txtPassword1.text.toString()

            val password2 = txtPassword2.text.toString()

            val rol = obtenerIdRol()

            if (nombre.isEmpty()
                || apellido.isEmpty()
                || numeroDocumento.isEmpty()
                || password1.isEmpty()
                || password2.isEmpty()
            ) {
                mostrarError(mensajeError, "Complete todos los campos")
                return@setOnClickListener
            }
            if (password1 != password2) {
                mostrarError(mensajeError, "Las contraseñas no coinciden")
                return@setOnClickListener
            }

            val usuarioDao = UsuarioDao(this)

            if (usuarioDao.existeDocumento(tipoDocumento, numeroDocumento)) {
                mostrarError(
                    mensajeError,
                    "Ya existe un usuario con ese Nro. de documento"
                )
                return@setOnClickListener
            }

            val usuario = Usuario(
                tipoDocumento = tipoDocumento,
                numeroDocumento = numeroDocumento,
                nombre = nombre,
                apellido = apellido,
                passwordUsuario = password1,
                idRol = rol,
                activo = true
            )

            val resultado = usuarioDao.insertar(usuario)

            if (resultado > 0) {

                val vista = layoutInflater.inflate(R.layout.dialog_registro, null)

                val txtMensajeRegistro = vista.findViewById<TextView>(R.id.txtMensajeRegistro)

                txtMensajeRegistro.text =
                    """
                       Usuario registrado correctamente
                       Nombre: $nombre
                       Apellido: $apellido
                       Documento: $numeroDocumento
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

                    txtNumeroDocumento.text.clear()

                    txtPassword1.text.clear()

                    txtPassword2.text.clear()
                }

            } else {
                mostrarError(
                    mensajeError,
                    "Error al crear el usuario"
                )
            }


        }

    }

    private fun inicializarVistas() {

        txtNombre = findViewById(R.id.nombre)
        txtApellido = findViewById(R.id.apellido)
        txtNumeroDocumento = findViewById(R.id.numeroDocumento)
        tipoDocumento = findViewById(R.id.tipoDocumento)
        tipoRol = findViewById(R.id.rol)
        txtPassword1 = findViewById(R.id.password1)
        txtPassword2 = findViewById(R.id.password2)
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

        tipoDocumento.setAdapter(adapter)

        tipoDocumento.setOnClickListener {
            tipoDocumento.showDropDown()
        }

        tipoDocumento.setText(opciones[0], false)
    }

    private fun configurarRoles() {

        val roles = listOf(
            "Empleado",
            "Administrador"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            roles
        )

        tipoRol.setAdapter(adapter)

        tipoRol.setOnClickListener {
            tipoRol.showDropDown()
        }

        tipoRol.setText(roles[0], false)
    }

    private fun obtenerIdRol(): Int {

        return when (
            tipoRol.text.toString()
        ) {
            "Administrador" -> 1
            else -> 2
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