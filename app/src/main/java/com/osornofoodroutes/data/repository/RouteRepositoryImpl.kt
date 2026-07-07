package com.osornofoodroutes.data.repository

import com.osornofoodroutes.data.remote.RouteApi
import com.osornofoodroutes.data.remote.dto.RouteRequest
import com.osornofoodroutes.domain.model.Route
import com.osornofoodroutes.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta del repositorio de rutas.
 * Patrón Repository: Abstrae el acceso a la base de datos Room.
 */
class RouteRepositoryImpl(
    private val routeApi: RouteApi
) : RouteRepository {

    override fun getAllRoutes(): Flow<List<Route>> {
        return kotlinx.coroutines.flow.flow {
            try {
                val routes = routeApi.getAllRoutes().map { it.toDomain() }
                emit(routes)
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }

    override fun getRoutesByUserId(userId: Long): Flow<List<Route>> {
        return kotlinx.coroutines.flow.flow {
            try {
                val routes = routeApi.getRoutesByUserId(userId).map { it.toDomain() }
                emit(routes)
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }

    override suspend fun getRouteById(id: Long): Route? {
        return try {
            routeApi.getRouteById(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertRoute(route: Route): Long {
        return try {
            val response = routeApi.createRoute(
                RouteRequest(
                    name = route.name,
                    description = route.description,
                    foodPlaceIds = route.foodPlaceIds,
                    estimatedTime = route.estimatedTime
                )
            )
            if (response.success) 1L else -1L
        } catch (e: Exception) {
            -1L
        }
    }

    override suspend fun updateRoute(route: Route) {
        try {
            routeApi.updateRoute(
                route.id,
                RouteRequest(
                    name = route.name,
                    description = route.description,
                    foodPlaceIds = route.foodPlaceIds,
                    estimatedTime = route.estimatedTime
                )
            )
        } catch (e: Exception) {
            // Ignorar por ahora
        }
    }

    override suspend fun deleteRoute(route: Route) {
        try {
            routeApi.deleteRoute(route.id)
        } catch (e: Exception) {
            // Ignorar
        }
    }
}
