package com.osornofoodroutes.data.remote.dto

import com.osornofoodroutes.domain.model.Route

data class RouteRequest(
    val name: String,
    val description: String,
    val foodPlaceIds: List<Long>,
    val estimatedTime: String = ""
)

data class RouteResponse(
    val id: Long,
    val name: String,
    val description: String,
    val userId: Long,
    val foodPlaceIds: List<Long>,
    val estimatedTime: String
) {
    fun toDomain(): Route {
        return Route(
            id = id,
            name = name,
            description = description,
            userId = userId,
            foodPlaceIds = foodPlaceIds,
            estimatedTime = estimatedTime
        )
    }
}
