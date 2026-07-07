package com.osornofoodroutes.data.remote

import com.osornofoodroutes.data.remote.dto.FoodPlaceRequest
import com.osornofoodroutes.data.remote.dto.FoodPlaceResponse
import com.osornofoodroutes.data.remote.dto.MessageResponse
import retrofit2.http.*

interface FoodPlaceApi {
    @GET("food-places")
    suspend fun getAllFoodPlaces(): List<FoodPlaceResponse>

    @GET("food-places")
    suspend fun getFoodPlacesByCategory(@Query("category") category: String): List<FoodPlaceResponse>

    @GET("food-places/{id}")
    suspend fun getFoodPlaceById(@Path("id") id: Long): FoodPlaceResponse

    @POST("food-places")
    suspend fun createFoodPlace(@Body request: FoodPlaceRequest): MessageResponse

    @PUT("food-places/{id}")
    suspend fun updateFoodPlace(@Path("id") id: Long, @Body request: FoodPlaceRequest): MessageResponse

    @DELETE("food-places/{id}")
    suspend fun deleteFoodPlace(@Path("id") id: Long): MessageResponse
}
