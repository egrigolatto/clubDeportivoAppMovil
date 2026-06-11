package com.example.clubdeportivo.activities


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

    private lateinit var txtUsuario: TextView
    private lateinit var txtRol: TextView
    private lateinit var btnCardRegistrarSocio: LinearLayout
    private lateinit var btnCardRegistrarNoSocio: LinearLayout
    private lateinit var btnCardPagarCuota: LinearLayout
    private lateinit var btnCardListaMorosos: LinearLayout
    private lateinit var btnCardRegistrarUsuario: LinearLayout
    private lateinit var btnSalirApp: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        inicializarVistas()

        // Obtener el ID del usuario de la actividad anterior
        val prefs = getSharedPreferences(
            "sesion",
            MODE_PRIVATE
        )

        val idUsuario = prefs.getInt(
            "idUsuario",
            -1
        )

        if (idUsuario != -1) {
            cargarUsuario(idUsuario)
        }

        configurarEventos()

    }

    private fun inicializarVistas() {
        btnCardRegistrarSocio = findViewById(R.id.cardRegistrarSocio)
        btnCardRegistrarNoSocio = findViewById(R.id.cardRegistrarNoSocio)
        btnCardPagarCuota = findViewById(R.id.cardPagarCuota)
        btnCardListaMorosos = findViewById(R.id.cardListaMorosos)
        btnCardRegistrarUsuario = findViewById(R.id.cardRegistrarUsuario)
        btnSalirApp = findViewById(R.id.btnSalirApp)
        txtUsuario = findViewById(R.id.txtUsuario)
        txtRol = findViewById(R.id.txtRol)
    }

    private fun cargarUsuario(
        idUsuario: Int
    ) {

        val usuarioDao = UsuarioDao(this)

        val usuario = usuarioDao.obtenerPorId(idUsuario)

        if (usuario == null) {
            return
        }

        // ocualta registrar usuario para empleados
        if (usuario.idRol != 1) {
            btnCardRegistrarUsuario.visibility =
                View.GONE
        }

        txtUsuario.text = "${usuario.nombre} ${usuario.apellido}"

        txtRol.text = obtenerNombreRol(usuario.idRol)
    }

    private fun configurarEventos() {
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
            val prefs = getSharedPreferences(
                "sesion",
                MODE_PRIVATE
            )

            prefs.edit().clear().apply()

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }

    }

    private fun obtenerNombreRol(
        idRol: Int
    ): String {

        return when (idRol) {
            1 -> "Administrador"
            2 -> "Empleado"
            else -> "Desconocido"
        }
    }

}
