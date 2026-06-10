package com.example.clubdeportivo.database.dao

import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.ui.model.MorosoUi

class MorosoDao(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun obtenerMorosos(): List<MorosoUi> {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT c.id_cliente, c.nombre, c.apellido
        FROM clientes c
        INNER JOIN cuotas_mensuales cm
            ON c.id_cliente = cm.id_cliente
        WHERE cm.estado = 'pendiente'
        GROUP BY c.id_cliente
        """.trimIndent(),
            null
        )

        val lista = mutableListOf<MorosoUi>()

        var index = 1

        while (cursor.moveToNext()) {

            lista.add(
                MorosoUi(
                    idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                    estado = "VENCIDO"
                )
            )

            index++
        }

        cursor.close()
        db.close()

        return lista
    }
}