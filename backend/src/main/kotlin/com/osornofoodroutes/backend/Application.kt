package com.osornofoodroutes.backend

import com.osornofoodroutes.backend.plugins.DatabaseFactory
import com.osornofoodroutes.backend.plugins.configureSecurity
import com.osornofoodroutes.backend.plugins.configureSerialization
import com.osornofoodroutes.backend.repository.FoodPlaceRepository
import com.osornofoodroutes.backend.repository.RouteRepository
import com.osornofoodroutes.backend.repository.UserRepository
import com.osornofoodroutes.backend.routes.authRoutes
import com.osornofoodroutes.backend.routes.foodPlaceRoutes
import com.osornofoodroutes.backend.routes.routeRoutes
import com.osornofoodroutes.backend.service.FoodPlaceService
import com.osornofoodroutes.backend.service.RouteService
import com.osornofoodroutes.backend.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import kotlinx.serialization.Serializable

@Serializable
data class RootResponse(
    val message: String,
    val version: String,
    val endpoints: List<String>
)

/**
 * Punto de entrada del servidor backend.
 * Usa EngineMain para cargar application.conf automáticamente.
 */
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // 1. Conectar a la base de datos Neon PostgreSQL
    DatabaseFactory.init(environment.config)

    // 2. Configurar serialización JSON
    configureSerialization()

    // 3. Configurar seguridad JWT
    configureSecurity()

    // 4. Configurar CORS (para que el frontend pueda conectarse)
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }

    // 5. Manejo de errores global
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                com.osornofoodroutes.backend.dto.MessageResponse(
                    message = cause.message ?: "Error interno del servidor", 
                    success = false
                )
            )
        }
    }

    // 6. Crear repositorios
    val userRepository = UserRepository()
    val foodPlaceRepository = FoodPlaceRepository()
    val routeRepository = RouteRepository()

    // 7. Crear servicios (inyectando repositorios)
    val userService = UserService(userRepository, environment.config)
    val foodPlaceService = FoodPlaceService(foodPlaceRepository)
    val routeService = RouteService(routeRepository, foodPlaceRepository)

    // 8. Configurar rutas API
    routing {
        // Ruta raíz para verificar que el servidor está funcionando
        get("/") {
            call.respond(
                RootResponse(
                    message = "🍽️ OsornoFoodRoutes API funcionando",
                    version = "1.0",
                    endpoints = listOf(
                        "POST /auth/register",
                        "POST /auth/login",
                        "GET /food-places",
                        "POST /food-places",
                        "PUT /food-places/{id}",
                        "DELETE /food-places/{id}",
                        "GET /routes (JWT requerido)",
                        "POST /routes (JWT requerido)",
                        "DELETE /routes/{id} (JWT requerido)"
                    )
                )
            )
        }

        // Rutas de autenticación (registro y login)
        authRoutes(userService)

        // Rutas de locales de comida (CRUD)
        foodPlaceRoutes(foodPlaceService)

        // Rutas de rutas gastronómicas (protegidas con JWT)
        routeRoutes(routeService)
    }

    println("🚀 Servidor OsornoFoodRoutes iniciado en http://localhost:8080")
}
