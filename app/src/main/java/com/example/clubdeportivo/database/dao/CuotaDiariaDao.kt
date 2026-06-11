package com.example.clubdeportivo.database.dao

import android.content.ContentValues
import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CuotaDiariaDao(
    context: Context
) {

    private val dbHelper = DatabaseHelper(context)


    /**
     * Registra el pago de una actividad diaria.
     *
     * Antes de insertar el registro verifica que
     * el cliente no haya abonado la misma actividad
     * en la fecha actual.
     *
     * @param idCliente ID del cliente.
     * @param idActividad ID de la actividad.
     * @param modoPago Medio de pago utilizado.
     *
     * @return ID del registro creado.
     *         Retorna -1 si el pago ya existe o si ocurre un error.
     */
    fun crearCuotaDiaria(
        idCliente: Int,
        idActividad: Int,
        modoPago: String
    ): Long {

        val db = dbHelper.writableDatabase

        val fecha = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())

        // 🔒 EVITAR DUPLICADO
        val cursor = db.rawQuery(
            """
        SELECT id_cuota
        FROM cuotas_diarias
        WHERE id_cliente = ?
        AND id_actividad = ?
        AND fecha = ?
        """.trimIndent(),
            arrayOf(
                idCliente.toString(),
                idActividad.toString(),
                fecha
            )
        )

        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return -1 // ya existe
        }

        cursor.close()

        val values = ContentValues().apply {
            put("id_cliente", idCliente)
            put("id_actividad", idActividad)
            put("fecha", fecha)
            put("modo_pago", modoPago)
        }

        val resultado = db.insert(
            "cuotas_diarias",
            null,
            values
        )

        db.close()

        return resultado
    }
}