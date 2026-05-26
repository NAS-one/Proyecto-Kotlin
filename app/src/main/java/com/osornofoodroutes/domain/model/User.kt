package com.osornofoodroutes.domain.model

/**
 * Modelo de dominio para un Usuario.
 * Principio SRP: Solo representa los datos del usuario.
 */
data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val password: String
)
