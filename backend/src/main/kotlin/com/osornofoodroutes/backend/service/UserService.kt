package com.osornofoodroutes.backend.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.osornofoodroutes.backend.dto.TokenResponse
import com.osornofoodroutes.backend.repository.UserRepository
import io.ktor.server.config.*
import java.util.*

/**
 * Servicio de usuarios.
 * Contiene las reglas de negocio y validaciones para autenticación.
 * Es el equivalente a los UseCases del frontend.
 */
class UserService(
    private val repository: UserRepository,
    private val config: ApplicationConfig
) {

    private val jwtSecret = config.property("jwt.secret").getString()
    private val jwtIssuer = config.property("jwt.issuer").getString()
    private val jwtAudience = config.property("jwt.audience").getString()

    /**
     * Registrar un nuevo usuario.
     * Reglas: email no puede estar vacío, no puede estar repetido,
     * la contraseña se hashea con BCrypt antes de guardarla.
     */
    fun register(name: String, email: String, password: String): Result<Int> {
        // Validación: campos no vacíos
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Todos los campos son obligatorios"))
        }

        // Validación: email con formato correcto
        if (!email.contains("@") || !email.contains(".")) {
            return Result.failure(Exception("El formato del email no es válido"))
        }

        // Validación: contraseña mínimo 6 caracteres
        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }

        // Validación: email no repetido
        val existingUser = repository.findByEmail(email)
        if (existingUser != null) {
            return Result.failure(Exception("Ya existe un usuario con ese email"))
        }

        // Hashear la contraseña con BCrypt (seguridad)
        val passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray())

        // Guardar en la base de datos
        val userId = repository.insert(name, email, passwordHash)
            ?: return Result.failure(Exception("Error al crear el usuario"))

        return Result.success(userId)
    }

    /**
     * Iniciar sesión y generar token JWT.
     * Reglas: verificar que el email existe, verificar contraseña con BCrypt,
     * y generar un token JWT válido por 24 horas.
     */
    fun login(email: String, password: String): Result<TokenResponse> {
        // Validación: campos no vacíos
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Email y contraseña son obligatorios"))
        }

        // Buscar usuario por email
        val user = repository.findByEmail(email)
            ?: return Result.failure(Exception("Credenciales incorrectas"))

        // Verificar contraseña con BCrypt
        val storedHash = user["passwordHash"] as String
        val isPasswordValid = BCrypt.verifyer()
            .verify(password.toCharArray(), storedHash)
            .verified

        if (!isPasswordValid) {
            return Result.failure(Exception("Credenciales incorrectas"))
        }

        // Generar token JWT (válido 24 horas)
        val token = JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withClaim("userId", user["id"] as Int)
            .withClaim("email", user["email"] as String)
            .withExpiresAt(Date(System.currentTimeMillis() + 86400000)) // 24 horas
            .sign(Algorithm.HMAC256(jwtSecret))

        return Result.success(
            TokenResponse(
                token = token,
                userId = user["id"] as Int,
                name = user["name"] as String,
                email = user["email"] as String
            )
        )
    }

    fun getUserById(id: Int) = repository.findById(id)

    fun getAllUsers() = repository.getAll()
}
