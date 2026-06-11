package com.example.clubdeportivo.database

object DatabaseContract {

    const val DATABASE_NAME = "club.db"
    const val DATABASE_VERSION = 10

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

    const val CREATE_ACTIVIDADES = """
    CREATE TABLE actividades(
        id_actividad INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre TEXT NOT NULL,
        monto REAL NOT NULL
        )
        """

    const val CREATE_CUOTAS_MENSUALES = """
    CREATE TABLE cuotas_mensuales(
        id_cuota INTEGER PRIMARY KEY AUTOINCREMENT,
        id_cliente INTEGER NOT NULL,
        periodo TEXT NOT NULL,
        monto REAL NOT NULL,
        fecha_emision TEXT NOT NULL,
        fecha_vencimiento TEXT NOT NULL,
        fecha_pago TEXT,
        modo_pago TEXT,
        promocion TEXT,
        estado TEXT NOT NULL DEFAULT 'pendiente',
    
        FOREIGN KEY(id_cliente)
            REFERENCES clientes(id_cliente),
    
        UNIQUE(id_cliente, periodo)
        )
        """

    const val CREATE_CUOTAS_DIARIAS = """
    CREATE TABLE cuotas_diarias(
        id_cuota INTEGER PRIMARY KEY AUTOINCREMENT,
        id_cliente INTEGER NOT NULL,
        id_actividad INTEGER NOT NULL,
        fecha TEXT NOT NULL,
        modo_pago TEXT,
        promocion TEXT,

        FOREIGN KEY(id_cliente)
            REFERENCES clientes(id_cliente),

        FOREIGN KEY(id_actividad)
            REFERENCES actividades(id_actividad)
    )
    """

}