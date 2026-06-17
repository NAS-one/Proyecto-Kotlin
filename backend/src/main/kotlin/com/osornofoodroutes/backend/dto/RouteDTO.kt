package com.osornofoodroutes.backend.dto

import kotlinx.serialization.Serializable

/**
 * DTOs para rutas gastronómicas.
 * Request: lo que envía el cliente para crear una ruta.
 * Response: lo que devuelve el servidor.
 */

@Serializable
data class RouteRequest(
    val name: String,
    val description: String,
    val foodPlaceIds: List<Int>,
    val estimatedTime: String = ""
)

@Serializable
data class RouteResponse(
    val id: Int,
    val name: String,
    val description: String,
    val userId: Int,
    val foodPlaceIds: List<Int>,
    val estimatedTime: String
)
