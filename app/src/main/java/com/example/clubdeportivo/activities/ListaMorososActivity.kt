package com.example.clubdeportivo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R
import com.example.clubdeportivo.adapters.MorosoAdapter
import com.example.clubdeportivo.database.dao.MorosoDao


class ListaMorososActivity : AppCompatActivity() {

    private lateinit var rvMorosos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_morosos)

        inicializarVistas()
        cargarMorosos()

        val btnVolver = findViewById<ImageView>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }

        val btnCancelar = findViewById<Button>(R.id.btnVolverMenu)
        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun inicializarVistas() {
        rvMorosos = findViewById(R.id.rvMorosos)
    }

    private fun cargarMorosos() {

        val dao = MorosoDao(this)

        val morosos = dao.obtenerMorosos()

        rvMorosos.layoutManager = LinearLayoutManager(this)

        rvMorosos.adapter = MorosoAdapter(morosos)
    }
}