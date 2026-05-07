package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroNoSociosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_no_socios)

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

        val btnRegistrarNoSocio = findViewById<Button>(R.id.btnRegistrarNoSocio)

        btnRegistrarNoSocio.setOnClickListener {

            val vista = layoutInflater.inflate(R.layout.dialog_registro, null)

            val btnAceptar = vista.findViewById<Button>(R.id.btnAceptar)

            val dialog = AlertDialog.Builder(this)
                .setView(vista)
                .create()

            dialog.show()

            btnAceptar.setOnClickListener {

                dialog.dismiss()

                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)

                finish()
            }
        }

        val tipoDoc = findViewById<AutoCompleteTextView>(R.id.tipoDocumento)
        val opciones = listOf("DNI", "Pasaporte", "CUIL")
        val adapterDoc = ArrayAdapter(this, android.R.layout.simple_list_item_1, opciones)
        tipoDoc.setAdapter(adapterDoc)
/*
        val modoPago = findViewById<AutoCompleteTextView>(R.id.modopago)
        val modos = listOf("EFECTIVO", "TARJETA")
        val adapterPag = ArrayAdapter(this, android.R.layout.simple_list_item_1, modos)
        modoPago.setAdapter(adapterPag)
*/
    }
}