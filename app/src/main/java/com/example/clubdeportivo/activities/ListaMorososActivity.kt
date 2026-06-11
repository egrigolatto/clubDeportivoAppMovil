package com.example.clubdeportivo.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R
import com.example.clubdeportivo.adapters.MorosoAdapter
import com.example.clubdeportivo.database.dao.MorosoDao
import com.google.android.material.card.MaterialCardView


class ListaMorososActivity : AppCompatActivity() {

    private lateinit var rvMorosos: RecyclerView
    private lateinit var btnVolver: ImageView
    private lateinit var btnCancelar: Button
    private lateinit var cardNoMorosos: MaterialCardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_morosos)

        inicializarVistas()
        cargarMorosos()

        btnVolver.setOnClickListener {
            finish()
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun inicializarVistas() {
        rvMorosos = findViewById(R.id.rvMorosos)
        btnVolver = findViewById(R.id.btnVolver)
        btnCancelar = findViewById(R.id.btnVolverMenu)
        cardNoMorosos = findViewById(R.id.cardNoResultados)
    }

    private fun cargarMorosos() {

        val dao = MorosoDao(this)

        val morosos = dao.obtenerMorosos()

        rvMorosos.layoutManager = LinearLayoutManager(this)

        rvMorosos.adapter = MorosoAdapter(morosos)

        if (morosos.isEmpty()) {
            cardNoMorosos.visibility = View.VISIBLE
            rvMorosos.visibility = View.GONE
        } else {
            cardNoMorosos.visibility = View.GONE
            rvMorosos.visibility = View.VISIBLE
        }
    }
}