package com.osornofoodroutes.backend.tables

import org.jetbrains.exposed.sql.Table

/**
 * Tabla de locales de comida en PostgreSQL.
 * Guarda toda la información de cada restaurante/café/local.
 */
object FoodPlacesTable : Table("food_places") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 150)
    val description = text("description")
    val category = varchar("category", 50)
    val address = varchar("address", 255)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val rating = float("rating").default(0f)
    val imageUrl = varchar("image_url", 500).default("")
    val phone = varchar("phone", 30).default("")
    val openingHours = varchar("opening_hours", 100).default("")

    override val primaryKey = PrimaryKey(id)
}
