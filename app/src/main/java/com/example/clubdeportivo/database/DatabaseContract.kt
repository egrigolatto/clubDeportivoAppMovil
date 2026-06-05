package com.example.clubdeportivo.database

object DatabaseContract {

    const val DATABASE_NAME = "club.db"
    const val DATABASE_VERSION = 1

    const val CREATE_ROLES = """
        CREATE TABLE roles(
            id_rol INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL UNIQUE
        )
    """

    const val CREATE_TIPOS_DOCUMENTO ="""
         CREATE TABLE tipos_documento(
             id_tipo_documento INTEGER PRIMARY KEY AUTOINCREMENT,
             nombre TEXT NOT NULL
         )
    """

    const val CREATE_USUARIOS = """
    CREATE TABLE usuarios(
        id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
        tipo_documento INTEGER NOT NULL,
        numero_documento TEXT NOT NULL,
        nombre TEXT NOT NULL,
        apellido TEXT NOT NULL,
        password_usuario TEXT NOT NULL,
        id_rol INTEGER NOT NULL,
        activo INTEGER NOT NULL DEFAULT 1,

        FOREIGN KEY(id_rol)
            REFERENCES roles(id_rol),

        FOREIGN KEY(tipo_documento)
            REFERENCES tipos_documento(id_tipo_documento),

        UNIQUE(tipo_documento, numero_documento)
    )
    """

    const val CREATE_CLIENTES = """
    CREATE TABLE clientes(
        id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
        fecha_alta DATETIME DEFAULT CURRENT_TIMESTAMP,
        nombre TEXT NOT NULL,
        apellido TEXT NOT NULL,
        tipo_documento INTEGER NOT NULL,
        numero_documento TEXT NOT NULL,
        email TEXT,
        telefono TEXT,
        es_socio INTEGER NOT NULL,
        apto_fisico INTEGER NOT NULL,
        estado TEXT DEFAULT 'activo',

        FOREIGN KEY(tipo_documento)
            REFERENCES tipos_documento(id_tipo_documento),

        UNIQUE(tipo_documento, numero_documento)
    )
    """
}