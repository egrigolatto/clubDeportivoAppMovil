package com.example.clubdeportivo.utils

object Validaciones {

    fun esDocumentoValido(dni: String): Boolean {
        return dni.matches(Regex("^\\d{6,8}$"))
    }

    fun esEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS
            .matcher(email)
            .matches()
    }

    fun esTelefonoValido(telefono: String): Boolean {
        return telefono.matches(Regex("^\\d{8,15}$"))
    }

    fun longitudMinima(
        texto: String,
        minimo: Int
    ): Boolean {
        return texto.length >= minimo
    }

}