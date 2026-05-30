package com.example.clubdeportivo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.LinearLayout
import android.widget.Button
import android.widget.TextView
import com.example.clubdeportivo.R
import com.example.clubdeportivo.database.dao.UsuarioDao


class MenuPrincipalActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)


        val txtUsuario =  findViewById<TextView>(R.id.txtUsuario)
        val txtRol =  findViewById<TextView>(R.id.txtRol)


        val idUsuario = intent.getIntExtra(
            "idUsuario",
            -1
        )

        val usuarioDao = UsuarioDao(this)

        val usuario = usuarioDao.obtenerPorId(idUsuario)

        if (usuario != null) {

           txtUsuario.text = "${usuario.nombre} ${usuario.apellido}"

           val rol = when(usuario.idRol) {
                1 -> "Administrador"
                2 -> "Empleado"
                else -> "Desconocido"
            }

            txtRol.text = rol
        }



        val btnCardRegistrarSocio = findViewById<LinearLayout>(R.id.cardRegistrarSocio)
        btnCardRegistrarSocio.setOnClickListener {
            val intent = Intent(this, RegistroSociosActivity::class.java)
            startActivity(intent)
        }

        val btnCardRegistrarNoSocio = findViewById<LinearLayout>(R.id.cardRegistrarNoSocio)
        btnCardRegistrarNoSocio.setOnClickListener {
            val intent = Intent(this, RegistroNoSociosActivity::class.java)
            startActivity(intent)
        }

        val btnCardPagarCuota = findViewById<LinearLayout>(R.id.cardPagarCuota)
        btnCardPagarCuota.setOnClickListener {
            val intent = Intent(this, PagarCuotaActivity::class.java)
            startActivity(intent)
        }

        val btnCardListaMorosos = findViewById<LinearLayout>(R.id.cardListaMorosos)
        btnCardListaMorosos.setOnClickListener {
            val intent = Intent(this, ListaMorososActivity::class.java)
            startActivity(intent)
        }

        val btnCardRegistrarAdmin = findViewById<LinearLayout>(R.id.cardRegistrarAdmin)
        btnCardRegistrarAdmin.setOnClickListener {
            val intent = Intent(this, RegistrarAdminActivity::class.java)
            startActivity(intent)
        }



        val btnSalirApp = findViewById<Button>(R.id.btnSalirApp)
        btnSalirApp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}
