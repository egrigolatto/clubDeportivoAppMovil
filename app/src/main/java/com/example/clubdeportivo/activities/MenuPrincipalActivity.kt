package com.example.clubdeportivo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
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

        val btnCardRegistrarSocio = findViewById<LinearLayout>(R.id.cardRegistrarSocio)
        val btnCardRegistrarNoSocio = findViewById<LinearLayout>(R.id.cardRegistrarNoSocio)
        val btnCardPagarCuota = findViewById<LinearLayout>(R.id.cardPagarCuota)
        val btnCardListaMorosos = findViewById<LinearLayout>(R.id.cardListaMorosos)
        val btnCardRegistrarUsuario = findViewById<LinearLayout>(R.id.cardRegistrarAdmin)
        val btnSalirApp = findViewById<Button>(R.id.btnSalirApp)


        val txtUsuario =  findViewById<TextView>(R.id.txtUsuario)
        val txtRol =  findViewById<TextView>(R.id.txtRol)


        val idUsuario = intent.getIntExtra(
            "idUsuario",
            -1
        )

        val usuarioDao = UsuarioDao(this)

        val usuario = usuarioDao.obtenerPorId(idUsuario)

        if (usuario != null) {

            if (usuario.idRol != 1) {
                // ocultar la card de registrar usuario a los empleados
                btnCardRegistrarUsuario.visibility =
                    View.GONE
            }

           txtUsuario.text = "${usuario.nombre} ${usuario.apellido}"

           val rol = when(usuario.idRol) {
                1 -> "Administrador"
                2 -> "Empleado"
                else -> "Desconocido"
            }

            txtRol.text = rol
        }

        btnCardRegistrarSocio.setOnClickListener {
            val intent = Intent(this, RegistroSociosActivity::class.java)
            startActivity(intent)
        }
        btnCardRegistrarNoSocio.setOnClickListener {
            val intent = Intent(this, RegistroNoSociosActivity::class.java)
            startActivity(intent)
        }
        btnCardPagarCuota.setOnClickListener {
            val intent = Intent(this, PagarCuotaActivity::class.java)
            startActivity(intent)
        }
        btnCardListaMorosos.setOnClickListener {
            val intent = Intent(this, ListaMorososActivity::class.java)
            startActivity(intent)
        }
        btnCardRegistrarUsuario.setOnClickListener {
            val intent = Intent(this, RegistrarUsuarioActivity::class.java)
            startActivity(intent)
        }
        btnSalirApp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}
