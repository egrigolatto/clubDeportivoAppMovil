package com.example.clubdeportivo.models

data class CuotaDiaria(
    val idCuota: Int = 0,
    val idCliente: Int,
    val idActividad: Int,
    val fecha: String,
    val modoPago: String? = null,
    val promocion: String? = null
)