package com.osornofoodroutes.domain.model

/**
 * Modelo de dominio para una Ruta de comida.
 * Agrupa varios locales de comida en un recorrido temático.
 */
data class Route(
    val id: Long = 0,
    val name: String,
    val description: String,
    val userId: Long,               // Usuario que creó la ruta
    val foodPlaceIds: List<Long>,   // Lista de IDs de locales en la ruta
    val estimatedTime: String = ""  // Ej: "2 horas"
)
