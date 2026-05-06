package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog


class PagarCuotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar_cuota)

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

        val dni = findViewById<EditText>(R.id.dniCliente)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)
        val mensajeError = findViewById<TextView>(R.id.mensajeError)

        btnConfirmar.setOnClickListener {
            val dniTexto = dni.text.toString().trim()
            if (dniTexto.isEmpty()) {
                mostrarError(mensajeError, "Ingrese un valor")
                return@setOnClickListener
            }

            when (dniTexto) {

                "1" -> {
                    val intent = Intent(this, PagarCuotaMensualActivity::class.java)
                    startActivity(intent)
                }

                "2" -> {
                    val intent = Intent(this, PagarCuotaDiariaActivity::class.java)
                    startActivity(intent)
                }

                "3" -> {
                    val vista = layoutInflater.inflate(R.layout.dialog_template, null)

                    val mensaje = vista.findViewById<TextView>(R.id.textDialog)

                    val btnAceptar = vista.findViewById<Button>(R.id.btnAceptar)

                    mensaje.text = "No registra deuda"

                    val dialog = AlertDialog.Builder(this)
                        .setView(vista)
                        .create()

                    dialog.show()

                    btnAceptar.setOnClickListener {

                        dialog.dismiss()

                    }
                }


                else -> {
                    val vista = layoutInflater.inflate(R.layout.dialog_template, null)

                    val mensaje = vista.findViewById<TextView>(R.id.textDialog)

                    val btnAceptar = vista.findViewById<Button>(R.id.btnAceptar)

                    mensaje.text = "El cliente no se encuentra registrado"

                    val dialog = AlertDialog.Builder(this)
                        .setView(vista)
                        .create()

                    dialog.show()

                    btnAceptar.setOnClickListener {

                        dialog.dismiss()

                    }
                }
            }
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