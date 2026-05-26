package com.osornofoodroutes.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenRouteApiService {
    @GET("v2/directions/driving-car")
    suspend fun getRoute(
        @Query("api_key") apiKey: String,
        @Query("start") start: String, // format: "lon,lat"
        @Query("end") end: String      // format: "lon,lat"
    ): OpenRouteResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.openrouteservice.org/"

    val instance: OpenRouteApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(OpenRouteApiService::class.java)
    }
}

data class OpenRouteResponse(
    val features: List<Feature>
)

data class Feature(
    val geometry: Geometry
)

data class Geometry(
    val coordinates: List<List<Double>>
)

