package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.widget.TextView


class PagarCuotaMensualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar_cuota_mensual)

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


    }
}