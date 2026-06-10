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

    fun obtenerPorDocumento(
        numeroDocumento: String
    ): Cliente? {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM clientes
        WHERE numero_documento = ?
        """.trimIndent(),
            arrayOf(
                numeroDocumento
            )
        )

        var cliente: Cliente? = null

        if (cursor.moveToFirst()) {

            cliente = Cliente(
                idCliente = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_cliente")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                apellido = cursor.getString(
                    cursor.getColumnIndexOrThrow("apellido")
                ),
                tipoDocumento = cursor.getInt(
                    cursor.getColumnIndexOrThrow("tipo_documento")
                ),
                numeroDocumento = cursor.getString(
                    cursor.getColumnIndexOrThrow("numero_documento")
                ),
                email = cursor.getString(
                    cursor.getColumnIndexOrThrow("email")
                ),
                telefono = cursor.getString(
                    cursor.getColumnIndexOrThrow("telefono")
                ),
                esSocio = cursor.getInt(
                    cursor.getColumnIndexOrThrow("es_socio")
                ) == 1,
                aptoFisico = cursor.getInt(
                    cursor.getColumnIndexOrThrow("apto_fisico")
                ) == 1,
                estado = cursor.getString(
                    cursor.getColumnIndexOrThrow("estado")
                )
            )
        }

        cursor.close()
        db.close()

        return cliente
    }

    fun obtenerPorId(
        idCliente: Int
    ): Cliente? {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM clientes
        WHERE id_cliente = ?
        """.trimIndent(),
            arrayOf(idCliente.toString())
        )

        var cliente: Cliente? = null

        if (cursor.moveToFirst()) {

            cliente = Cliente(
                idCliente = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_cliente")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                apellido = cursor.getString(
                    cursor.getColumnIndexOrThrow("apellido")
                ),
                tipoDocumento = cursor.getInt(
                    cursor.getColumnIndexOrThrow("tipo_documento")
                ),
                numeroDocumento = cursor.getString(
                    cursor.getColumnIndexOrThrow("numero_documento")
                ),
                email = cursor.getString(
                    cursor.getColumnIndexOrThrow("email")
                ),
                telefono = cursor.getString(
                    cursor.getColumnIndexOrThrow("telefono")
                ),
                esSocio = cursor.getInt(
                    cursor.getColumnIndexOrThrow("es_socio")
                ) == 1,
                aptoFisico = cursor.getInt(
                    cursor.getColumnIndexOrThrow("apto_fisico")
                ) == 1,
                estado = cursor.getString(
                    cursor.getColumnIndexOrThrow("estado")
                )
            )
        }

        cursor.close()
        db.close()

        return cliente
    }

}