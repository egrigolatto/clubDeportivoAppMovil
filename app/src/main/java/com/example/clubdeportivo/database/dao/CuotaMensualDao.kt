package com.example.clubdeportivo.database.dao

import android.content.ContentValues
import android.content.Context
import com.example.clubdeportivo.database.DatabaseHelper
import com.example.clubdeportivo.models.CuotaMensual
import com.example.clubdeportivo.utils.Config
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CuotaMensualDao(
    context: Context
) {

    private val dbHelper = DatabaseHelper(context)

    fun crearPrimeraCuota(
        idCliente: Int,
        modoPago: String
    ): Long {

        val db = dbHelper.writableDatabase

        val calendario = Calendar.getInstance()

        val fechaEmision = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(calendario.time)

        val anio = calendario.get(Calendar.YEAR)

        val mes = calendario.get(Calendar.MONTH) + 1

        val periodo = String.format(
            "%04d-%02d",
            anio,
            mes
        )

        calendario.add(
            Calendar.MONTH,
            1
        )

        val fechaVencimiento = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(calendario.time)

        val values = ContentValues().apply {

            put("id_cliente", idCliente)

            put("periodo", periodo)

            put(
                "monto",
                Config.MONTO_CUOTA_MENSUAL
            )

            put(
                "fecha_emision",
                fechaEmision
            )

            put(
                "fecha_vencimiento",
                fechaVencimiento
            )

            put(
                "fecha_pago",
                fechaEmision
            )

            put(
                "modo_pago",
                modoPago
            )

            put(
                "estado",
                "pagada"
            )
        }

        val resultado = db.insert(
            "cuotas_mensuales",
            null,
            values
        )

        db.close()

        return resultado
    }

    fun obtenerPendientes(
        idCliente: Int
    ): List<CuotaMensual> {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT *
            FROM cuotas_mensuales
            WHERE id_cliente = ?
            AND estado = 'pendiente'
            ORDER BY periodo
            """.trimIndent(),
            arrayOf(idCliente.toString())
        )

        val cuotas = mutableListOf<CuotaMensual>()

        while (cursor.moveToNext()) {

            cuotas.add(
                CuotaMensual(
                    idCuota = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id_cuota")
                    ),

                    idCliente = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id_cliente")
                    ),
                    periodo = cursor.getString(
                        cursor.getColumnIndexOrThrow("periodo")
                    ),

                    monto = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("monto")
                    ),

                    fechaEmision = cursor.getString(
                        cursor.getColumnIndexOrThrow("fecha_emision")
                    ),

                    fechaVencimiento = cursor.getString(
                        cursor.getColumnIndexOrThrow("fecha_vencimiento")
                    ),

                    fechaPago = cursor.getString(
                        cursor.getColumnIndexOrThrow("fecha_pago")
                    ),

                    modoPago = cursor.getString(
                        cursor.getColumnIndexOrThrow("modo_pago")
                    ),

                    promocion = cursor.getString(
                        cursor.getColumnIndexOrThrow("promocion")
                    ),

                    estado = cursor.getString(
                        cursor.getColumnIndexOrThrow("estado")
                    )
                )
            )
        }

        cursor.close()
        db.close()

        return cuotas
    }

    fun obtenerUltimaCuotaVigente(
        idCliente: Int
    ): CuotaMensual? {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM cuotas_mensuales
        WHERE id_cliente = ?
        AND estado = 'pagada'
        ORDER BY fecha_vencimiento DESC
        LIMIT 1
        """.trimIndent(),
            arrayOf(idCliente.toString())
        )

        var cuota: CuotaMensual? = null

        if (cursor.moveToFirst()) {

            cuota = CuotaMensual(
                idCuota = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_cuota")
                ),
                idCliente = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id_cliente")
                ),
                monto = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("monto")
                ),
                periodo = cursor.getString(
                    cursor.getColumnIndexOrThrow("periodo")
                ),
                fechaEmision = cursor.getString(
                    cursor.getColumnIndexOrThrow("fecha_emision")
                ),
                fechaVencimiento = cursor.getString(
                    cursor.getColumnIndexOrThrow("fecha_vencimiento")
                ),
                fechaPago = cursor.getString(
                    cursor.getColumnIndexOrThrow("fecha_pago")
                ),
                modoPago = cursor.getString(
                    cursor.getColumnIndexOrThrow("modo_pago")
                ),
                estado = cursor.getString(
                    cursor.getColumnIndexOrThrow("estado")
                ),
                promocion = cursor.getString(
                    cursor.getColumnIndexOrThrow("promocion")
                )
            )
        }

        cursor.close()
        db.close()

        return cuota
    }
    fun pagarPendientes(
        idCliente: Int,
        modoPago: String
    ): Int {

        val db = dbHelper.writableDatabase

        val fechaPago = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())

        val values = ContentValues().apply {

            put("estado", "pagada")

            put("modo_pago", modoPago)

            put("fecha_pago", fechaPago)
        }

        val filasActualizadas = db.update(
            "cuotas_mensuales",
            values,
            "id_cliente = ? AND estado = ?",
            arrayOf(
                idCliente.toString(),
                "pendiente"
            )
        )

        db.close()

        return filasActualizadas
    }


}