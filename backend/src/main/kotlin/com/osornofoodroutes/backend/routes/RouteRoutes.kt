package com.osornofoodroutes.backend.routes

import com.osornofoodroutes.backend.dto.MessageResponse
import com.osornofoodroutes.backend.dto.RouteRequest
import com.osornofoodroutes.backend.service.RouteService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Rutas de rutas gastronómicas (API).
 * Protegidas con JWT - solo usuarios autenticados pueden usarlas.
 * GET    /routes        → Listar rutas del usuario autenticado
 * POST   /routes        → Crear nueva ruta
 * DELETE /routes/{id}   → Eliminar ruta
 */
fun Route.routeRoutes(routeService: RouteService) {

    // Estas rutas requieren estar autenticado con JWT
    authenticate("auth-jwt") {
        route("/routes") {

            // GET /routes → Listar rutas del usuario autenticado
            get {
                val userId = call.principal<JWTPrincipal>()!!
                    .payload.getClaim("userId").asInt()

                val routes = routeService.getByUserId(userId)
                call.respond(HttpStatusCode.OK, routes)
            }

            // GET /routes/all → Listar todas las rutas (para admin/debug)
            get("/all") {
                val routes = routeService.getAll()
                call.respond(HttpStatusCode.OK, routes)
            }

            // POST /routes → Crear nueva ruta
            post {
                val userId = call.principal<JWTPrincipal>()!!
                    .payload.getClaim("userId").asInt()

                val request = call.receive<RouteRequest>()

                routeService.create(userId, request)
                    .onSuccess { id ->
                        call.respond(
                            HttpStatusCode.Created,
                            MessageResponse("Ruta creada exitosamente con id: $id")
                        )
                    }
                    .onFailure { error ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            MessageResponse(error.message ?: "Error al crear", success = false)
                        )
                    }
            }

            // DELETE /routes/{id} → Eliminar ruta
            delete("/{id}") {
                val userId = call.principal<JWTPrincipal>()!!
                    .payload.getClaim("userId").asInt()

                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respond(
                        HttpStatusCode.BadRequest,
                        MessageResponse("ID inválido", success = false)
                    )

                routeService.delete(id, userId)
                    .onSuccess {
                        call.respond(HttpStatusCode.OK, MessageResponse("Ruta eliminada exitosamente"))
                    }
                    .onFailure { error ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            MessageResponse(error.message ?: "Error al eliminar", success = false)
                        )
                    }
            }
        }
    }
}
