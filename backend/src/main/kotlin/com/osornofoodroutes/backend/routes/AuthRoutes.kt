package com.osornofoodroutes.backend.routes

import com.osornofoodroutes.backend.dto.LoginRequest
import com.osornofoodroutes.backend.dto.MessageResponse
import com.osornofoodroutes.backend.dto.RegisterRequest
import com.osornofoodroutes.backend.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Rutas de autenticación (API).
 * Usa peticiones HTTP POST para registro y login.
 * POST /auth/register → Crear nuevo usuario
 * POST /auth/login    → Iniciar sesión y obtener token JWT
 */
fun Route.authRoutes(userService: UserService) {

    route("/auth") {

        // POST /auth/register → Registrar usuario nuevo
        post("/register") {
            val request = call.receive<RegisterRequest>()

            userService.register(request.name, request.email, request.password)
                .onSuccess { userId ->
                    call.respond(
                        HttpStatusCode.Created,
                        MessageResponse("Usuario registrado exitosamente con id: $userId")
                    )
                }
                .onFailure { error ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        MessageResponse(error.message ?: "Error al registrar", success = false)
                    )
                }
        }

        // POST /auth/login → Login y devolver token JWT
        post("/login") {
            val request = call.receive<LoginRequest>()

            userService.login(request.email, request.password)
                .onSuccess { tokenResponse ->
                    call.respond(HttpStatusCode.OK, tokenResponse)
                }
                .onFailure { error ->
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        MessageResponse(error.message ?: "Error al iniciar sesión", success = false)
                    )
                }
        }
    }
}
