package com.example.clubdeportivo.database.dao

import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.TipoDocumento

class TipoDocumentoDao(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun obtenerTodos(): List<TipoDocumento> {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT *
            FROM tipos_documento
            ORDER BY nombre
            """.trimIndent(),
            null
        )

        val tiposDocumento = mutableListOf<TipoDocumento>()

        while (cursor.moveToNext()) {

            val tipoDocumento = TipoDocumento(
                idTipoDocumento = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_tipo_documento")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                )
            )

            tiposDocumento.add(tipoDocumento)
        }

        cursor.close()
        db.close()

        return tiposDocumento
    }

    fun obtenerPorNombre(
        nombre: String
    ): TipoDocumento? {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM tipos_documento
        WHERE nombre = ?
        """.trimIndent(),
            arrayOf(nombre)
        )

        var tipoDocumento: TipoDocumento? = null

        if (cursor.moveToFirst()) {

            tipoDocumento = TipoDocumento(
                idTipoDocumento = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_tipo_documento")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                )
            )
        }

        cursor.close()
        db.close()

        return tipoDocumento
    }
}