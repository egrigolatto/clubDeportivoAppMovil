package com.example.clubdeportivo.database.dao

import android.content.ContentValues
import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Usuario


class UsuarioDao(
    context: Context
) {

    private val dbHelper = DatabaseHelper(context)

    /**
     * Valida las credenciales de un usuario activo.
     *
     * @param numeroDocumento Documento del usuario.
     * @param password Contraseña ingresada.
     * @return Usuario si las credenciales son válidas, null en caso contrario.
     */
    fun login(
        numeroDocumento: String,
        password: String
    ): Usuario? {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT *
            FROM usuarios
            WHERE numero_documento = ?
            AND password_usuario = ?
            AND activo = 1
            """.trimIndent(),
            arrayOf(
                numeroDocumento,
                password
            )
        )

        var usuario: Usuario? = null

        if (cursor.moveToFirst()) {

            usuario = Usuario(
                idUsuario = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_usuario")
                ),
                tipoDocumento = cursor.getInt(
                    cursor.getColumnIndexOrThrow("tipo_documento")
                ),
                numeroDocumento = cursor.getString(
                    cursor.getColumnIndexOrThrow("numero_documento")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                apellido = cursor.getString(
                    cursor.getColumnIndexOrThrow("apellido")
                ),
                passwordUsuario = cursor.getString(
                    cursor.getColumnIndexOrThrow("password_usuario")
                ),
                idRol = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_rol")
                ),
                activo = cursor.getInt(
                    cursor.getColumnIndexOrThrow("activo")
                ) == 1
            )
        }

        cursor.close()
        db.close()

        return usuario
    }

    /**
     * Obtiene un usuario por su identificador.
     *
     * @param idUsuario Identificador del usuario.
     * @return Usuario encontrado o null si no existe.
     */
    fun obtenerPorId(
        idUsuario: Int
    ): Usuario? {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT *
            FROM usuarios
            WHERE id_usuario = ?
            """.trimIndent(),
            arrayOf(idUsuario.toString())
        )

        var usuario: Usuario? = null

        if (cursor.moveToFirst()) {

            usuario = Usuario(
                idUsuario = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_usuario")
                ),
                tipoDocumento = cursor.getInt(
                    cursor.getColumnIndexOrThrow("tipo_documento")
                ),
                numeroDocumento = cursor.getString(
                    cursor.getColumnIndexOrThrow("numero_documento")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                ),
                apellido = cursor.getString(
                    cursor.getColumnIndexOrThrow("apellido")
                ),
                passwordUsuario = cursor.getString(
                    cursor.getColumnIndexOrThrow("password_usuario")
                ),
                idRol = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_rol")
                ),
                activo = cursor.getInt(
                    cursor.getColumnIndexOrThrow("activo")
                ) == 1
            )
        }

        cursor.close()
        db.close()

        return usuario
    }

    /**
     * Verifica si ya existe un usuario registrado
     * con el tipo y número de documento indicado.
     *
     * @param tipoDocumento Tipo de documento.
     * @param numeroDocumento Número de documento.
     * @return true si existe, false en caso contrario.
     */
    fun existeDocumento(
        tipoDocumento: Int,
        numeroDocumento: String
    ): Boolean {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT 1
            FROM usuarios
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
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario Usuario a registrar.
     * @return Id generado si la inserción fue exitosa.
     * Retorna -1 en caso de error.
     */
    fun insertar(
        usuario: Usuario
    ): Long {

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {

            put(
                "numero_documento",
                usuario.numeroDocumento
            )

            put(
                "tipo_documento",
                usuario.tipoDocumento
            )

            put(
                "nombre",
                usuario.nombre
            )

            put(
                "apellido",
                usuario.apellido
            )

            put(
                "password_usuario",
                usuario.passwordUsuario
            )

            put(
                "id_rol",
                usuario.idRol
            )

            put(
                "activo",
                if (usuario.activo) 1 else 0
            )
        }

        val resultado = db.insert(
            "usuarios",
            null,
            values
        )

        db.close()

        return resultado
    }
}

