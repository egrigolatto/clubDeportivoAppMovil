package com.example.clubdeportivo.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DatabaseContract.DATABASE_NAME,
    null,
    DatabaseContract.DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {

        // Crear tablas
        db.execSQL(DatabaseContract.CREATE_ROLES)
        db.execSQL(DatabaseContract.CREATE_TIPOS_DOCUMENTO)
        db.execSQL(DatabaseContract.CREATE_USUARIOS)
        db.execSQL(DatabaseContract.CREATE_CLIENTES)
        db.execSQL(DatabaseContract.CREATE_ACTIVIDADES)
        db.execSQL(DatabaseContract.CREATE_CUOTAS_MENSUALES)
        db.execSQL(DatabaseContract.CREATE_CUOTAS_DIARIAS)


        // Insertar datos iniciales
        insertarDatosIniciales(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS roles")
        db.execSQL("DROP TABLE IF EXISTS tipos_documento ")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS clientes ")
        db.execSQL("DROP TABLE IF EXISTS actividades ")
        db.execSQL("DROP TABLE IF EXISTS cuotas_mensuales ")
        db.execSQL("DROP TABLE IF EXISTS cuotas_diarias ")

        onCreate(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {

        insertarRoles(db)

        insertarTiposDocumento(db)

        insertarAdministrador(db)

        insertarCliente(db)

        insertarActividades(db)

        insertarCuotasPrueba(db)
    }

    private fun insertarRoles(db: SQLiteDatabase) {

        db.execSQL("""
            INSERT INTO roles(nombre)
            VALUES ('Administrador')
        """)

        db.execSQL("""
            INSERT INTO roles(nombre)
            VALUES ('Empleado')
        """.trimIndent())
    }

    private fun insertarTiposDocumento(db: SQLiteDatabase) {

        db.execSQL("""
            INSERT INTO tipos_documento(nombre)
            VALUES ('DNI')
        """)

        db.execSQL("""
            INSERT INTO tipos_documento(nombre)
            VALUES ('Pasaporte')
        """.trimIndent())
    }

    private fun insertarAdministrador(db: SQLiteDatabase) {

        db.execSQL("""
        INSERT INTO usuarios(
            nombre,
            apellido,
            tipo_documento,
            numero_documento,
            password_usuario,
            id_rol,
            activo
        )
        VALUES(
            'Administrador',
            'Principal',
            1,
            '1234',
            '1234',
            1,
            1
        )
    """.trimIndent())
    }



    private fun insertarCliente(db: SQLiteDatabase){

        db.execSQL("""
        INSERT INTO clientes(
            nombre,
            apellido,
            tipo_documento,
            numero_documento,
            email,
            telefono,
            es_socio,
            apto_fisico,
            estado
        )
        VALUES(
            'Juan',
            'Perez',
            1,
            '99999999',
            'juan@gmail.com',
            '3434123456',
            1,
            1,
            'activo'
        )
    """.trimIndent())
    }

    private fun insertarActividades(db: SQLiteDatabase) {

        db.execSQL("""
        INSERT INTO actividades(
            nombre,
            monto
        )
        VALUES
            ('Natación', 15000),
            ('Musculación', 12000),
            ('Fútbol', 10000),
            ('Yoga', 8000),
            ('Pilates', 9000)
    """.trimIndent())
    }

    private fun insertarCuotasPrueba(
        db: SQLiteDatabase
    ) {

        db.execSQL("""
    INSERT INTO cuotas_mensuales(
        id_cliente,
        periodo,
        monto,
        fecha_emision,
        fecha_vencimiento,
        fecha_pago,
        modo_pago,
        promocion,
        estado
    )
    VALUES
    (
        1,
        '2026-03',
        50000,
        '2026-03-01',
        '2026-04-10',
        NULL,
        NULL,
        NULL,
        'pendiente'
    ),
    (
        1,
        '2026-04',
        50000,
        '2026-04-01',
        '2026-05-10',
        NULL,
        NULL,
        NULL,
        'pendiente'
    ),
    (
        1,
        '2026-05',
        50000,
        '2026-05-01',
        '2026-06-10',
        NULL,
        NULL,
        NULL,
        'pendiente'
    )
    """.trimIndent())
    }
}