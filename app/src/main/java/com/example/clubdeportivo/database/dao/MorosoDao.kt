package com.example.clubdeportivo.database.dao

import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.ui.model.MorosoUi

class MorosoDao(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    /**
     * Obtiene la lista de socios que poseen
     * al menos una cuota mensual pendiente.
     *
     * Se consideran morosos todos los clientes
     * que tengan registros en cuotas_mensuales
     * con estado = 'pendiente'.
     *
     * @return Lista de morosos para mostrar
     *         en el RecyclerView.
     */
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

        while (cursor.moveToNext()) {

            lista.add(
                MorosoUi(
                    idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                    estado = "VENCIDO"
                )
            )
        }

        cursor.close()
        db.close()

        return lista
    }
}