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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val dbHelper = DatabaseHelper(this)

        val db = dbHelper.writableDatabase

        db.close()

        val dni = "1234"
        val contrasenia = "1234"


        val usuario = findViewById<EditText>(R.id.usuario)
        val password = findViewById<EditText>(R.id.password)
        val mensajeErrorLogin = findViewById<TextView>(R.id.mensajeErrorLogin)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)


        btnIngresar.setOnClickListener {

            val usuarioTexto = usuario.text.toString().trim()
            val passwordTexto = password.text.toString().trim()

            if (usuarioTexto.isEmpty() || passwordTexto.isEmpty()) {
                mostrarError(mensajeErrorLogin, "Complete los campos")
                return@setOnClickListener
            }

            if (usuarioTexto != dni || passwordTexto != contrasenia) {
                mostrarError(mensajeErrorLogin, "Datos incorrectos")
                return@setOnClickListener
            }

            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)

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

