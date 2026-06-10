package com.example.clubdeportivo.models

data class CuotaMensual(
    val idCuota: Int = 0,
    val idCliente: Int,
    val periodo: String,
    val monto: Double,
    val fechaEmision: String,
    val fechaVencimiento: String,
    val fechaPago: String?,
    val modoPago: String?,
    val promocion: String?,
    val estado: String
)