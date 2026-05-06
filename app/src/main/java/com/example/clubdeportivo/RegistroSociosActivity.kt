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
import android.widget.EditText
import android.widget.CheckBox
import android.widget.TextView


class RegistroSociosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_socios)

        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val tipoDoc = findViewById<AutoCompleteTextView>(R.id.tipoDocumento)
        val tipoDni = tipoDoc.text.toString()
        val dni = findViewById<EditText>(R.id.dni)
        val email = findViewById<EditText>(R.id.email)
        val telefono = findViewById<EditText>(R.id.telefono)
        val aptoFisico = findViewById<CheckBox>(R.id.aptoFisico)

        // variables para validaciones

        val modoPago = findViewById<AutoCompleteTextView>(R.id.modopago)
        val modopago = modoPago.text.toString()

        val montoPagar = findViewById<TextView>(R.id.montopagar)
        montoPagar.text = "$ 20.000"

        val btnVolver = findViewById<ImageView>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }
        val btnCancelar = findViewById<LinearLayout>(R.id.btnCancelar)
        btnCancelar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

        val btnRegistrarSocio = findViewById<Button>(R.id.btnRegistrarSocio)
        btnRegistrarSocio.setOnClickListener {
            val intent = Intent(this, CarnetDigitalActivity::class.java)
            startActivity(intent)
        }




        val opciones = listOf("DNI", "Pasaporte",)
        val adapterDoc = ArrayAdapter(this, android.R.layout.simple_list_item_1, opciones)
        tipoDoc.setAdapter(adapterDoc)


        val modos = listOf("EFECTIVO", "TARJETA")
        val adapterPag = ArrayAdapter(this, android.R.layout.simple_list_item_1, modos)
        modoPago.setAdapter(adapterPag)

    }
}