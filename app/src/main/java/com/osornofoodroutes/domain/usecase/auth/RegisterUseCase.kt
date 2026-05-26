package com.osornofoodroutes.domain.usecase.auth

import com.osornofoodroutes.domain.model.User
import com.osornofoodroutes.domain.repository.UserRepository

/**
 * Caso de uso para registrar un nuevo usuario.
 * Principio SRP: Solo se encarga de la lógica de registro.
 */
class RegisterUseCase(private val userRepository: UserRepository) {

    sealed class Result {
        data class Success(val userId: Long) : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(name: String, email: String, password: String): Result {
        // Validaciones
        if (name.isBlank()) {
            return Result.Error("El nombre es obligatorio")
        }
        if (email.isBlank() || !email.contains("@")) {
            return Result.Error("Ingresa un email válido")
        }
        if (password.length < 6) {
            return Result.Error("La contraseña debe tener al menos 6 caracteres")
        }

        val user = User(
            name = name,
            email = email,
            password = password
        )
        val userId = userRepository.register(user)
        return if (userId > 0) {
            Result.Success(userId)
        } else {
            Result.Error("Error al registrar. El email ya puede estar en uso.")
        }
    }
}
