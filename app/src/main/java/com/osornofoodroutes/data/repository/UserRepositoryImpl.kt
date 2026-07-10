package com.osornofoodroutes.data.repository

import com.osornofoodroutes.data.remote.AuthApi
import com.osornofoodroutes.data.remote.BackendApiClient
import com.osornofoodroutes.data.remote.dto.LoginRequest
import com.osornofoodroutes.data.remote.dto.RegisterRequest
import com.osornofoodroutes.domain.model.User
import com.osornofoodroutes.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta del repositorio de usuarios.
 * Principio LSP: Puede sustituir cualquier UserRepository.
 * Patrón Repository: Abstrae el acceso a la fuente de datos.
 */
class UserRepositoryImpl(
    private val authApi: AuthApi
) : UserRepository {

    override suspend fun register(user: User): Long {
        return try {
            val response = authApi.register(
                RegisterRequest(
                    name = user.name,
                    email = user.email,
                    password = user.password
                )
            )
            // Retrofit lanza excepción si el código no es 2xx, así que si llegamos aquí, fue exitoso.
            // Gson asigna 'false' por defecto si el campo success se omitió en el JSON del backend.
            1L
        } catch (e: Exception) {
            -1L // Email duplicado u otro error
        }
    }

    override suspend fun login(email: String, password: String): User? {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            // Guardar el token en el cliente para futuras peticiones
            BackendApiClient.authToken = response.token
            
            User(
                id = response.userId.toLong(),
                name = response.name,
                email = response.email,
                password = ""
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getUserById(id: Long): User? {
        return null // No implementado en backend
    }

    override suspend fun updateUser(user: User) {
        // No implementado en backend
    }

    override suspend fun deleteUser(user: User) {
        // No implementado en backend
    }

    override fun getAllUsers(): Flow<List<User>> {
        return kotlinx.coroutines.flow.emptyFlow() // No implementado en backend
    }
}
