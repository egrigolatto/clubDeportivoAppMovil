package com.example.clubdeportivo.ui.model

data class MorosoUi(
    val idCliente: Int,
    val nombre: String,
    val apellido: String,
    val estado: String = "VENCIDO"
)