package com.example.clubdeportivo.models

data class Cliente(
    val idCliente: Int = 0,
    val fechaAlta: String? = null,
    val nombre: String,
    val apellido: String,
    val tipoDocumento: Int,
    val numeroDocumento: String,
    val email: String?,
    val telefono: String?,
    val esSocio: Boolean,
    val aptoFisico: Boolean,
    val estado: String = "activo"
)