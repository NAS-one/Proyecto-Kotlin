package com.osornofoodroutes.domain.model

/**
 * Modelo de dominio para un Local de Comida.
 * Contiene la información de un restaurante/local en Osorno.
 */
data class FoodPlace(
    val id: Long = 0,
    val name: String,
    val description: String,
    val category: String,          // Ej: "Restaurante", "Café", "Pastelería", "Comida Rápida"
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 0f,        // 0.0 a 5.0
    val imageUrl: String = "",
    val phone: String = "",
    val openingHours: String = ""  // Ej: "Lun-Vie 09:00-21:00"
)
