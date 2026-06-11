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
import com.example.clubdeportivo.database.dao.RolDao
import com.example.clubdeportivo.database.dao.TipoDocumentoDao
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
    private lateinit var btnCancelar : Button
    private lateinit var btnVolver : ImageView
    private lateinit var btnRegistrarUsuario : Button


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
        btnCancelar.setOnClickListener {
            finish()
        }

        // BOTON REGISTRAR USUARIO
        btnRegistrarUsuario.setOnClickListener {
            registrarUsuario()
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
        btnCancelar = findViewById(R.id.btnCancelar)
        btnVolver = findViewById(R.id.btnVolver)
        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario)
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

    private fun configurarRoles() {

        val rolDao = RolDao(this)
        val roles = rolDao.obtenerTodos()
        val nombres = roles.map { it.nombre }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombres
        )

        tipoRol.setAdapter(adapter)

        tipoRol.setOnClickListener {
            tipoRol.showDropDown()
        }

        if (nombres.isNotEmpty()) {
            tipoRol.setText(nombres[0], false)
        }
    }

    private fun registrarUsuario() {

        val tipoDocumentoDao = TipoDocumentoDao(this)
        val tipoDocumentoSeleccionado =  tipoDocumentoDao.obtenerPorNombre(
            tipoDocumento.text.toString()
        )
        if (tipoDocumentoSeleccionado == null) {
            mostrarError(
                mensajeError,
                "Seleccione un tipo de documento válido"
            )
            return
        }

        val rolDao = RolDao(this)
        val rolSeleccionado = rolDao.obtenerPorNombre(
            tipoRol.text.toString()
        )
        if (rolSeleccionado == null) {
            mostrarError(
                mensajeError,
                "Seleccione un rol válido"
            )
            return
        }

        val idTipoDocumento = tipoDocumentoSeleccionado.idTipoDocumento
        val idRol = rolSeleccionado.idRol
        val nombre = txtNombre.text.toString().trim()
        val apellido = txtApellido.text.toString().trim()
        val numeroDocumento = txtNumeroDocumento.text.toString().trim()
        val password1 = txtPassword1.text.toString()
        val password2 = txtPassword2.text.toString()


        if (nombre.isEmpty()
            || apellido.isEmpty()
            || numeroDocumento.isEmpty()
            || password1.isEmpty()
            || password2.isEmpty()
        ) {
            mostrarError(mensajeError, "Complete todos los campos")
            return
        }
        if (password1 != password2) {
            mostrarError(mensajeError, "Las contraseñas no coinciden")
            return
        }

        val usuarioDao = UsuarioDao(this)

        if (usuarioDao.existeDocumento(idTipoDocumento, numeroDocumento)) {
            mostrarError(
                mensajeError,
                "Ya existe un usuario con ese Nro. de documento"
            )
            return
        }

        val usuario = Usuario(
            tipoDocumento = idTipoDocumento,
            numeroDocumento = numeroDocumento,
            nombre = nombre,
            apellido = apellido,
            passwordUsuario = password1,
            idRol = idRol,
            activo = true
        )

        val resultado = usuarioDao.insertar(usuario)

        if (resultado < 0) {
            mostrarError(
                mensajeError,
                "Error al crear el usuario"
            )
            return
        }

        mostrarDialogoRegistro(
            nombre,
            apellido,
            numeroDocumento
        )

    }

    private fun mostrarDialogoRegistro(
        nombre: String,
        apellido: String,
        numeroDocumento: String ) {

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

    }

    private fun mostrarError(textView: TextView, mensaje: String) {
        textView.text = mensaje
        textView.visibility = View.VISIBLE

        textView.postDelayed({
            textView.visibility = View.INVISIBLE
        }, 3000)
    }
}