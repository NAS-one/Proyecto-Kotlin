package com.osornofoodroutes.backend.dto

import kotlinx.serialization.Serializable

/**
 * DTOs de autenticación.
 * TokenRequest: lo que envía el usuario para pedir un token.
 * TokenResponse: el token JWT que devuelve el servidor.
 */

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class TokenResponse(
    val token: String,
    val userId: Int,
    val name: String,
    val email: String
)

@Serializable
data class MessageResponse(
    val message: String,
    val success: Boolean = true
)
