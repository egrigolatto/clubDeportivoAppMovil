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

    const val CREATE_USUARIOS = """
    CREATE TABLE usuarios(
        id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
        dni TEXT NOT NULL UNIQUE,
        nombre TEXT NOT NULL,
        apellido TEXT NOT NULL,
        password_usuario TEXT NOT NULL,
        id_rol INTEGER NOT NULL,
        activo INTEGER NOT NULL DEFAULT 1,
        FOREIGN KEY(id_rol)
            REFERENCES roles(id_rol)
    )
    """
}