package com.example.clubdeportivo.ui.model

data class ActividadUi(
    val idActividad: Int,
    val nombre: String,
    val monto: Double,
    var seleccionada: Boolean = false
)