package com.osornofoodroutes.backend.tables

import org.jetbrains.exposed.sql.Table

/**
 * Tabla de rutas gastronómicas en PostgreSQL.
 * Cada ruta pertenece a un usuario y contiene una lista de locales.
 */
object RoutesTable : Table("routes") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 150)
    val description = text("description")
    val userId = integer("user_id").references(UsersTable.id)
    val foodPlaceIds = varchar("food_place_ids", 500)  // IDs separados por coma
    val estimatedTime = varchar("estimated_time", 50).default("")

    override val primaryKey = PrimaryKey(id)
}
