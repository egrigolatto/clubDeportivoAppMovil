package com.example.clubdeportivo.database.dao

import android.content.ContentValues
import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Cliente

class ClienteDao(
    context: Context
) {
    private val dbHelper = DatabaseHelper(context)

    /**
     * Inserta un nuevo cliente en la base de datos.
     *
     * @param cliente Objeto Cliente con los datos a registrar.
     *
     * @return ID de la fila insertada.
     *         Retorna -1 si ocurre un error durante la inserción.
     */
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

    /**
     * Verifica si ya existe un cliente con el tipo y número
     * de documento indicados.
     *
     * Se utiliza para evitar registros duplicados.
     *
     * @param tipoDocumento ID del tipo de documento.
     * @param numeroDocumento Número de documento a verificar.
     *
     * @return true si el documento ya existe.
     *         false si no existe.
     */
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

    /**
     * Obtiene un cliente a partir de su número de documento.
     *
     * @param numeroDocumento Número de documento a buscar.
     *
     * @return Objeto Cliente si existe.
     *         null si no se encuentra ningún registro.
     */
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

    /**
     * Obtiene un cliente a partir de su ID.
     *
     * @param idCliente Identificador único del cliente.
     *
     * @return Objeto Cliente si existe.
     *         null si no se encuentra el cliente.
     */
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