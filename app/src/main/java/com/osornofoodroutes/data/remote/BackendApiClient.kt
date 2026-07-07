package com.osornofoodroutes.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit configurado para conectar con el backend de Ktor.
 */
object BackendApiClient {
    // 10.0.2.2 es la IP especial en el emulador de Android para acceder a localhost de la computadora
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Variable estática para guardar el token temporalmente mientras la app está abierta.
    // Para un proyecto más avanzado esto se guardaría en DataStore o SharedPreferences.
    var authToken: String? = null

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        
        // Si tenemos un token guardado, lo inyectamos en el Header de Authorization
        authToken?.let { token ->
            builder.header("Authorization", "Bearer $token")
        }
        
        chain.proceed(builder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val foodPlaceApi: FoodPlaceApi by lazy {
        retrofit.create(FoodPlaceApi::class.java)
    }

    val routeApi: RouteApi by lazy {
        retrofit.create(RouteApi::class.java)
    }
}
