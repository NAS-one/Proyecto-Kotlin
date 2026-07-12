package com.osornofoodroutes.backend.repository

import com.osornofoodroutes.backend.tables.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Repositorio de usuarios.
 * Habla directamente con la base de datos y le hace consultas SQL.
 */
class UserRepository {

    fun insert(name: String, email: String, passwordHash: String): Int? = transaction {
        val statement = UsersTable.insert {
            it[UsersTable.name] = name
            it[UsersTable.email] = email
            it[UsersTable.passwordHash] = passwordHash
        }
        try {
            statement[UsersTable.id]
        } catch (e: Exception) {
            UsersTable.select { UsersTable.email eq email }.singleOrNull()?.get(UsersTable.id)
        }
    }

    fun findByEmail(email: String) = transaction {
        UsersTable.select { UsersTable.email eq email }
            .map { toMap(it) }
            .singleOrNull()
    }

    fun findById(id: Int) = transaction {
        UsersTable.select { UsersTable.id eq id }
            .map { toMap(it) }
            .singleOrNull()
    }

    fun getAll() = transaction {
        UsersTable.selectAll().map { toMap(it) }
    }

    fun deleteById(id: Int) = transaction {
        UsersTable.deleteWhere { UsersTable.id eq id }
    }

    private fun toMap(row: ResultRow) = mapOf(
        "id" to row[UsersTable.id],
        "name" to row[UsersTable.name],
        "email" to row[UsersTable.email],
        "passwordHash" to row[UsersTable.passwordHash]
    )
}

