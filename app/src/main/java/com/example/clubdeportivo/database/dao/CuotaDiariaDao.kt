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

        fun yaPagoHoy(
            idCliente: Int,
            idActividad: Int
        ): Boolean {

            val db = dbHelper.readableDatabase

            val fechaHoy = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date())

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
                    fechaHoy
                )
            )

            val existe = cursor.moveToFirst()

            cursor.close()
            db.close()

            return existe
        }



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