package com.osornofoodroutes.data.remote.dto

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class TokenResponse(
    val token: String,
    val userId: Long,
    val name: String,
    val email: String
)

data class MessageResponse(
    val message: String,
    val success: Boolean = true
)
