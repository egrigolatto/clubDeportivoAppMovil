package com.example.clubdeportivo.models

data class Usuario(
    val idUsuario: Int,
    val dni: String,
    val nombre: String,
    val apellido: String,
    val passwordUsuario: String,
    val idRol: Int,
    val activo: Boolean
)