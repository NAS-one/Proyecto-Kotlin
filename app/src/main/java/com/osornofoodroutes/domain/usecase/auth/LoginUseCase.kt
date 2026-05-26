package com.osornofoodroutes.domain.usecase.auth

import com.osornofoodroutes.domain.model.User
import com.osornofoodroutes.domain.repository.UserRepository

/**
 * Caso de uso para iniciar sesión.
 * Principio SRP: Solo se encarga de la lógica de login.
 */
class LoginUseCase(private val userRepository: UserRepository) {

    sealed class Result {
        data class Success(val user: User) : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(email: String, password: String): Result {
        if (email.isBlank() || password.isBlank()) {
            return Result.Error("Email y contraseña son obligatorios")
        }
        val user = userRepository.login(email, password)
        return if (user != null) {
            Result.Success(user)
        } else {
            Result.Error("Credenciales incorrectas")
        }
    }
}
