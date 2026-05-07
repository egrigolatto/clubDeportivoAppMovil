package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.LinearLayout
import android.widget.Button
import android.widget.TextView



class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        //val rol = "ADMINISTRADOR"
        //val usuario = "user_01"

        //val rolUsuario = findViewById<TextView>(R.id.rol)
        //val user = findViewById<TextView>(R.id.usuario)

        //rolUsuario.text = rol
        //user.text = usuario

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
