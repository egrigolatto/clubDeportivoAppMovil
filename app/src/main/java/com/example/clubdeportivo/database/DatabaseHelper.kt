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
        db.execSQL(DatabaseContract.CREATE_USUARIOS)

        // Insertar datos iniciales
        insertarDatosIniciales(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS roles")

        onCreate(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {

        insertarRoles(db)

        insertarAdministrador(db)
    }

    private fun insertarRoles(db: SQLiteDatabase) {

        db.execSQL("""
            INSERT INTO roles(nombre)
            VALUES ('Administrador')
        """)

        db.execSQL("""
            INSERT INTO roles(nombre)
            VALUES ('Empleado')
        """)
    }

    private fun insertarAdministrador(db: SQLiteDatabase) {

        db.execSQL("""
        INSERT INTO usuarios(
            dni,
            nombre,
            apellido,
            password_usuario,
            id_rol,
            activo
        )
        VALUES(
            '12345678',
            'Administrador',
            'Principal',
            '123456',
            1,
            1
        )
    """)
    }
}