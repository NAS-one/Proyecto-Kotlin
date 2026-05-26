package com.osornofoodroutes.data.repository

import com.osornofoodroutes.data.local.dao.UserDao
import com.osornofoodroutes.data.local.toDomain
import com.osornofoodroutes.data.local.toEntity
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
    private val userDao: UserDao
) : UserRepository {

    override suspend fun register(user: User): Long {
        return try {
            userDao.insert(user.toEntity())
        } catch (e: Exception) {
            -1L // Email duplicado u otro error
        }
    }

    override suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)?.toDomain()
    }

    override suspend fun getUserById(id: Long): User? {
        return userDao.getUserById(id)?.toDomain()
    }

    override suspend fun updateUser(user: User) {
        userDao.update(user.toEntity())
    }

    override suspend fun deleteUser(user: User) {
        userDao.delete(user.toEntity())
    }

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { list ->
            list.map { it.toDomain() }
        }
    }
}
