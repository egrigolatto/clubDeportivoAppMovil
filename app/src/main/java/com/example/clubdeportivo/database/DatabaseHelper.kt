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

        onCreate(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {

        insertarRoles(db)

        insertarTiposDocumento(db)

        insertarAdministrador(db)

        insertarCliente(db)
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
}