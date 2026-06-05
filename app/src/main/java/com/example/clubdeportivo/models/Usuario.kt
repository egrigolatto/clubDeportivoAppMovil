package com.example.clubdeportivo.models

data class Usuario(
    val idUsuario: Int = 0,
    val tipoDocumento: Int,
    val numeroDocumento: String,
    val nombre: String,
    val apellido: String,
    val passwordUsuario: String,
    val idRol: Int,
    val activo: Boolean = true
)