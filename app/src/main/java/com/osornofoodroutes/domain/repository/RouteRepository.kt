package com.osornofoodroutes.domain.repository

import com.osornofoodroutes.domain.model.Route
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del Repositorio de Rutas.
 * Principio DIP: Abstracción para acceso a datos de rutas.
 */
interface RouteRepository {
    fun getAllRoutes(): Flow<List<Route>>
    fun getRoutesByUserId(userId: Long): Flow<List<Route>>
    suspend fun getRouteById(id: Long): Route?
    suspend fun insertRoute(route: Route): Long
    suspend fun updateRoute(route: Route)
    suspend fun deleteRoute(route: Route)
}
