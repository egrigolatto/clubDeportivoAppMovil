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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Fuerza la creación de la base de datos si aún no existe
        val dbHelper = DatabaseHelper(this)
        dbHelper.writableDatabase
        dbHelper.close()

        val usuarioDao = UsuarioDao(this)



        val usuario = findViewById<EditText>(R.id.usuario)
        val password = findViewById<EditText>(R.id.password)
        val mensajeErrorLogin = findViewById<TextView>(R.id.mensajeErrorLogin)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)


        btnIngresar.setOnClickListener {

            val dni = usuario.text.toString().trim()
            val contrasenia = password.text.toString().trim()


            if (dni.isEmpty() || contrasenia.isEmpty()) {
                mostrarError(mensajeErrorLogin, "Complete los campos")
                return@setOnClickListener
            }

            val usuarioLogueado = usuarioDao.login(
                dni,
                contrasenia
            )

            if (usuarioLogueado == null) {
                mostrarError(
                    mensajeErrorLogin,
                    "Datos incorrectos"
                )
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Bienvenido ${usuarioLogueado.nombre}",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(
                this,
                MenuPrincipalActivity::class.java
            )

            intent.putExtra(
                "idUsuario",
                usuarioLogueado.idUsuario
            )

            startActivity(intent)
            finish()

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

