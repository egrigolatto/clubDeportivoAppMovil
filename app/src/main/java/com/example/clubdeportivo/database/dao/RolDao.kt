package com.example.clubdeportivo.database.dao

import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Rol

class RolDao(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun obtenerTodos(): List<Rol> {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT *
            FROM roles
            ORDER BY nombre
            """.trimIndent(),
            null
        )

        val roles = mutableListOf<Rol>()

        while (cursor.moveToNext()) {

            val rol = Rol(
                idRol = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_rol")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                )
            )

            roles.add(rol)
        }

        cursor.close()
        db.close()

        return roles
    }

    fun obtenerPorNombre(
        nombre: String
    ): Rol? {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM roles
        WHERE nombre = ?
        """.trimIndent(),
            arrayOf(nombre)
        )

        var rol: Rol? = null

        if (cursor.moveToFirst()) {

            rol = Rol(
                idRol = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_rol")
                ),
                nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
                )
            )
        }

        cursor.close()
        db.close()

        return rol
    }
}