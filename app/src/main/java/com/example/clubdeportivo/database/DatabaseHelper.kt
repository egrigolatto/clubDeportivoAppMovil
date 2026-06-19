package com.example.clubdeportivo.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * Responsable de crear y actualizar la base de datos SQLite.
 *
 * Se ejecuta automáticamente la primera vez que la aplicación
 * accede a la base de datos.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DatabaseContract.DATABASE_NAME,
    null,
    DatabaseContract.DATABASE_VERSION
) {

    /**
     * Se ejecuta únicamente cuando la base de datos
     * se crea por primera vez.
     *
     * Crea todas las tablas e inserta los datos iniciales.
     */
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


    /**
     * Se ejecuta cuando aumenta la versión de la base de datos.
     *
     * Elimina todas las tablas existentes y las vuelve a crear.
     *
     */
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

    /**
     * Inserta todos los registros necesarios para que
     * el sistema pueda comenzar a funcionar.
     */
    private fun insertarDatosIniciales(db: SQLiteDatabase) {

        insertarRoles(db)

        insertarTiposDocumento(db)

        insertarAdministrador(db)

        insertarClienteSocio(db)

        insertarClienteNoSocio(db)

        insertarActividades(db)

        insertarCuotasPrueba(db)

        insertarEmpleado(db)
    }

    /**
     * Crea los roles básicos del sistema.
     *
     * 1 = Administrador
     * 2 = Empleado
     */
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

    /**
     * Crea el usuario administrador inicial.
     *
     * Documento: 1234
     * Contraseña: 1234
     *
     */
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
            'Usuario',
            'Administrador',
            1,
            '1111',
            '1234',
            1,
            1
        )
    """.trimIndent())
    }

    /**
     * Crea el usuario empleado inicial.
     *
     * Documento: 1234
     * Contraseña: 1234
     *
     */
    private fun insertarEmpleado(db: SQLiteDatabase) {

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
            'Usuario',
            'Empleado',
            1,
            '2222',
            '1234',
            2,
            1
        )
    """.trimIndent())
    }



    /**
     * Inserta un cliente socio de prueba.
     *
     */
    private fun insertarClienteSocio(db: SQLiteDatabase){

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
        ),
        (
            'Pedro',
            'Ruiz',
            1,
            '77777777',
            'pedro@gmail.com',
            '3434123454',
            1,
            1,
            'activo'
        ),
        (
            'Miguel',
            'Ramirez',
            1,
            '66666666',
            'miguel@gmail.com',
            '3434123545',
            1,
            1,
            'activo'
        )
    """.trimIndent())
    }

    /**
     * Inserta un cliente no socio de prueba.
     *
     */
    private fun insertarClienteNoSocio(db: SQLiteDatabase){

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
            'Lucas',
            'Gonzales',
            1,
            '88888888',
            'lucas@gmail.com',
            '3434123456',
            0,
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

    /**
     * Inserta una cuota pendiente de prueba.
     *
     * Se utiliza para probar la generación automática
     * de cuotas vencidas y la lista de morosos.
     */
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
        '2026-03-10',
        '2026-04-10',
        NULL,
        NULL,
        NULL,
        'pendiente'
    ),
     (
        2,
        '2026-04',
        50000,
        '2026-04-10',
        '2026-05-10',
        NULL,
        NULL,
        NULL,
        'pendiente'
    ),
    (
        3,
        '2026-01',
        50000,
        '2026-01-10',
        '2026-02-10',
        NULL,
        NULL,
        NULL,
        'pendiente'
    )
    """.trimIndent())
    }
}