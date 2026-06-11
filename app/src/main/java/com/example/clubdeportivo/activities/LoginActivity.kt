package com.example.clubdeportivo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.R
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.database.dao.UsuarioDao

class LoginActivity : AppCompatActivity() {

    private lateinit var txtDocumento: EditText
    private lateinit var txtContrasenia: EditText
    private lateinit var txtMensajeError: TextView
    private lateinit var btnIngresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inicializarVistas()
        inicializarBaseDeDatos()

        btnIngresar.setOnClickListener {
            realizarLogin()
        }
    }

    private fun inicializarVistas() {
        txtDocumento = findViewById(R.id.usuario)
        txtContrasenia = findViewById(R.id.password)
        txtMensajeError = findViewById(R.id.mensajeErrorLogin)
        btnIngresar = findViewById(R.id.btnIngresar)
    }

    private fun inicializarBaseDeDatos() {
        val dbHelper = DatabaseHelper(this)
        dbHelper.writableDatabase
        dbHelper.close()
    }

    private fun realizarLogin() {

        val documento = txtDocumento.text.toString().trim()
        val contrasenia = txtContrasenia.text.toString().trim()

        if (documento.isEmpty() || contrasenia.isEmpty()) {
            mostrarError("Complete los campos")
            return
        }

        val usuarioDao = UsuarioDao(this)

        val usuarioLogueado = usuarioDao.login(
            documento,
            contrasenia
        )

        if (usuarioLogueado == null) {

            mostrarError("Datos incorrectos")

            return
        }

        // Guardar sesión
        val prefs = getSharedPreferences(
            "sesion",
            MODE_PRIVATE
        )

        prefs.edit()
            .putInt(
                "idUsuario",
                usuarioLogueado.idUsuario
            )
            .apply()

        Toast.makeText(
            this,
            "Bienvenido ${usuarioLogueado.nombre}",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(
            Intent(
                this,
                MenuPrincipalActivity::class.java
            )
        )

        finish()
    }

    private fun mostrarError(
        mensaje: String
    ) {

        txtMensajeError.text = mensaje

        txtMensajeError.visibility = View.VISIBLE

        txtMensajeError.postDelayed({

            txtMensajeError.visibility = View.INVISIBLE

        }, 3000)
    }
}

