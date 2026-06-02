package com.example.clubdeportivo.database.dao

import android.content.ContentValues
import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Cliente

class ClienteDao(
    context: Context
) {
    private val dbHelper = DatabaseHelper(context)

    fun insertar(
        cliente: Cliente
    ): Long {

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {

            put(
                "nombre",
                cliente.nombre
            )

            put(
                "apellido",
                cliente.apellido
            )

            put(
                "tipo_documento",
                cliente.tipoDocumento
            )

            put(
                "numero_documento",
                cliente.numeroDocumento
            )

            put(
                "email",
                cliente.email
            )

            put(
                "telefono",
                cliente.telefono
            )

            put(
                "es_socio",
                if (cliente.esSocio) 1 else 0
            )

            put(
                "apto_fisico",
                if (cliente.aptoFisico) 1 else 0
            )

            put(
                "estado",
                cliente.estado
            )
        }

        val resultado = db.insert(
            "clientes",
            null,
            values
        )

        db.close()

        return resultado
    }

    fun existeDocumento(
        tipoDocumento: Int,
        numeroDocumento: String
    ): Boolean {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT 1
            FROM clientes
            WHERE tipo_documento = ?
            AND numero_documento = ?
            LIMIT 1
            """.trimIndent(),
            arrayOf(
                tipoDocumento.toString(),
                numeroDocumento
            )
        )

        val existe = cursor.moveToFirst()

        cursor.close()
        db.close()

        return existe
    }

}