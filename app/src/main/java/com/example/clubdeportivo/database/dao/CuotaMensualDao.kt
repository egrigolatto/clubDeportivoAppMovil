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

    /**
     * Crea la primera cuota mensual de un socio.
     *
     * La cuota se registra automáticamente como pagada
     * al momento de realizar la inscripción.
     *
     * @param idCliente ID del socio.
     * @param modoPago Medio de pago utilizado.
     *
     * @return ID de la cuota creada.
     *         Retorna -1 si ocurre un error.
     */
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

    /**
     * Obtiene todas las cuotas pendientes de pago
     * de un socio.
     *
     * Las cuotas se devuelven ordenadas por período
     * de forma ascendente.
     *
     * @param idCliente ID del socio.
     *
     * @return Lista de cuotas pendientes.
     *         Si no existen deudas retorna una lista vacía.
     */
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

    /**
     * Obtiene la última cuota pagada de un socio.
     *
     * Se utiliza para determinar la vigencia del carnet
     * digital y la fecha de vencimiento más reciente.
     *
     * @param idCliente ID del socio.
     *
     * @return Última cuota pagada o null si no existe.
     */
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
        ORDER BY periodo DESC
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
    /**
     * Marca todas las cuotas pendientes del socio
     * como pagadas.
     *
     * Actualiza:
     * - estado
     * - fecha de pago
     * - modo de pago
     *
     * @param idCliente ID del socio.
     * @param modoPago Medio de pago utilizado.
     *
     * @return Cantidad de filas actualizadas.
     */
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

    /**
     * Obtiene la última cuota registrada para un socio,
     * independientemente de su estado.
     *
     * Puede devolver una cuota pagada o pendiente.
     *
     * Se utiliza como base para generar nuevas cuotas.
     *
     * @param idCliente ID del socio.
     *
     * @return Última cuota registrada o null.
     */
    fun obtenerUltimaCuota(
        idCliente: Int
    ): CuotaMensual? {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT *
        FROM cuotas_mensuales
        WHERE id_cliente = ?
        ORDER BY periodo DESC
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
        }

        cursor.close()
        db.close()

        return cuota
    }

    /**
     * Crea una nueva cuota pendiente.
     *
     * Método interno utilizado por el sistema para
     * generar automáticamente cuotas vencidas.
     *
     * @param idCliente ID del socio.
     * @param periodo Período correspondiente.
     * @param fechaEmision Fecha de emisión.
     * @param fechaVencimiento Fecha límite de pago.
     */
    private fun crearCuotaPendiente(
        idCliente: Int,
        periodo: String,
        fechaEmision: String,
        fechaVencimiento: String
    ) {

        val db = dbHelper.writableDatabase

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
                "estado",
                "pendiente"
            )
        }

        db.insert(
            "cuotas_mensuales",
            null,
            values
        )

        db.close()
    }

    /**
     * Genera automáticamente las cuotas pendientes
     * que un socio debería tener hasta la fecha actual.
     *
     * El proceso toma la última cuota registrada y,
     * mientras su fecha de vencimiento sea anterior
     * al día actual, genera nuevas cuotas pendientes
     * mes a mes.
     *
     * @param idCliente ID del socio.
     */
    fun actualizarCuotasPendientes(
        idCliente: Int
    ) {

        val formato = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        )

        val hoy = Date()

        var ultimaCuota =
            obtenerUltimaCuota(idCliente)

        while (
            ultimaCuota != null &&
            formato.parse(
                ultimaCuota.fechaVencimiento
            )!!.before(hoy)
        ) {

            val calendario =
                Calendar.getInstance()

            calendario.time =
                formato.parse(
                    ultimaCuota.fechaVencimiento
                )!!

            val fechaEmision =
                ultimaCuota.fechaVencimiento

            calendario.add(
                Calendar.MONTH,
                1
            )

            val fechaVencimiento =
                formato.format(
                    calendario.time
                )

            val partes =
                ultimaCuota.periodo.split("-")

            val anio =
                partes[0].toInt()

            val mes =
                partes[1].toInt()

            val siguiente =
                Calendar.getInstance()

            siguiente.set(
                anio,
                mes - 1,
                1
            )

            siguiente.add(
                Calendar.MONTH,
                1
            )

            val nuevoPeriodo =
                String.format(
                    "%04d-%02d",
                    siguiente.get(Calendar.YEAR),
                    siguiente.get(Calendar.MONTH) + 1
                )

            crearCuotaPendiente(
                idCliente,
                nuevoPeriodo,
                fechaEmision,
                fechaVencimiento
            )

            ultimaCuota =
                obtenerUltimaCuota(idCliente)
        }
    }


}