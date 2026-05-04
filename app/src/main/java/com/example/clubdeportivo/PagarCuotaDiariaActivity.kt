package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PagarCuotaDiariaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar_cuota_diaria)

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

        val btnPagarCuota = findViewById<Button>(R.id.btnPagarCuota)
        btnPagarCuota.setOnClickListener {

            val vista = layoutInflater.inflate(R.layout.dialog_template, null)

            val mensaje = vista.findViewById<TextView>(R.id.textDialog)

            val btnAceptar = vista.findViewById<Button>(R.id.btnAceptar)

            mensaje.text = "Pago exitoso"

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

        val modoPago = findViewById<AutoCompleteTextView>(R.id.modopago)
        val modos = listOf("EFECTIVO", "TARJETA")
        val adapterPag = ArrayAdapter(this, android.R.layout.simple_list_item_1, modos)
        modoPago.setAdapter(adapterPag)
    }
}