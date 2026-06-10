package com.example.clubdeportivo.database.dao

import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.Actividad

class ActividadDao(
    context: Context
) {

        private val dbHelper = DatabaseHelper(context)

        fun obtenerTodas(): List<Actividad> {

            val db = dbHelper.readableDatabase

            val cursor = db.rawQuery(
                """
            SELECT *
            FROM actividades
            ORDER BY nombre
            """.trimIndent(),
                null
            )

            val lista = mutableListOf<Actividad>()

            while (cursor.moveToNext()) {

                val actividad = Actividad(
                    idActividad = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id_actividad")
                    ),
                    nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow("nombre")
                    ),
                    monto = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("monto")
                    )
                )

                lista.add(actividad)
            }

            cursor.close()
            db.close()

            return lista
        }
    }
