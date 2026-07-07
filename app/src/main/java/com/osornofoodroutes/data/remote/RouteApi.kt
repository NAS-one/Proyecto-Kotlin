package com.osornofoodroutes.data.remote

import com.osornofoodroutes.data.remote.dto.MessageResponse
import com.osornofoodroutes.data.remote.dto.RouteRequest
import com.osornofoodroutes.data.remote.dto.RouteResponse
import retrofit2.http.*

interface RouteApi {
    @GET("routes")
    suspend fun getAllRoutes(): List<RouteResponse>

    @GET("routes/user/{userId}")
    suspend fun getRoutesByUserId(@Path("userId") userId: Long): List<RouteResponse>

    @GET("routes/{id}")
    suspend fun getRouteById(@Path("id") id: Long): RouteResponse

    @POST("routes")
    suspend fun createRoute(@Body request: RouteRequest): MessageResponse

    @PUT("routes/{id}")
    suspend fun updateRoute(@Path("id") id: Long, @Body request: RouteRequest): MessageResponse

    @DELETE("routes/{id}")
    suspend fun deleteRoute(@Path("id") id: Long): MessageResponse
}
