package com.example.clubdeportivo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout





class RegistroSociosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_socios)

        val btnVolver = findViewById<ImageView>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        btnCancelar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

        val btnRegistrarSocio = findViewById<Button>(R.id.btnRegistrarSocio)
        btnRegistrarSocio.setOnClickListener {
            val intent = Intent(this, CarnetDigitalActivity::class.java)
            startActivity(intent)
        }



        val tipoDoc = findViewById<AutoCompleteTextView>(R.id.tipoDocumento)
        val opciones = listOf("DNI", "Pasaporte", "CUIL")
        val adapterDoc = ArrayAdapter(this, android.R.layout.simple_list_item_1, opciones)
        tipoDoc.setAdapter(adapterDoc)

        val modoPago = findViewById<AutoCompleteTextView>(R.id.modo_pago)
        val modos = listOf("EFECTIVO", "TARJETA")
        val adapterPag = ArrayAdapter(this, android.R.layout.simple_list_item_1, modos)
        modoPago.setAdapter(adapterPag)

    }
}