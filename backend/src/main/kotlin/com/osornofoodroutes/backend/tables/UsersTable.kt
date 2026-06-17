package com.osornofoodroutes.backend.tables

import org.jetbrains.exposed.sql.Table

/**
 * Tabla de usuarios en PostgreSQL.
 * Almacena nombre, email (único) y contraseña hasheada.
 */
object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val email = varchar("email", 150).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)

    override val primaryKey = PrimaryKey(id)
}
