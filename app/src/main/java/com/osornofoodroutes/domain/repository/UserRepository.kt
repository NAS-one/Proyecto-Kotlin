package com.osornofoodroutes.domain.repository

import com.osornofoodroutes.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del Repositorio de Usuarios.
 * Principio DIP: La capa de dominio depende de esta abstracción,
 * no de la implementación concreta.
 * Principio ISP: Solo métodos relacionados con usuarios.
 */
interface UserRepository {
    suspend fun register(user: User): Long
    suspend fun login(email: String, password: String): User?
    suspend fun getUserById(id: Long): User?
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    fun getAllUsers(): Flow<List<User>>
}
