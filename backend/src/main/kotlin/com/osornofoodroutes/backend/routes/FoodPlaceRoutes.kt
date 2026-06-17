package com.osornofoodroutes.backend.routes

import com.osornofoodroutes.backend.dto.FoodPlaceRequest
import com.osornofoodroutes.backend.dto.MessageResponse
import com.osornofoodroutes.backend.service.FoodPlaceService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Rutas de locales de comida (API).
 * CRUD completo con peticiones HTTP.
 * GET    /food-places        → Listar todos
 * GET    /food-places/{id}   → Obtener uno por ID
 * POST   /food-places        → Crear nuevo
 * PUT    /food-places/{id}   → Actualizar existente
 * DELETE /food-places/{id}   → Eliminar
 */
fun Route.foodPlaceRoutes(foodPlaceService: FoodPlaceService) {

    route("/food-places") {

        // GET /food-places → Listar todos los locales
        get {
            val category = call.request.queryParameters["category"]
            val places = if (category != null) {
                foodPlaceService.getByCategory(category)
            } else {
                foodPlaceService.getAll()
            }
            call.respond(HttpStatusCode.OK, places)
        }

        // GET /food-places/{id} → Obtener un local por ID
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    MessageResponse("ID inválido", success = false)
                )

            val place = foodPlaceService.getById(id)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    MessageResponse("Local no encontrado", success = false)
                )

            call.respond(HttpStatusCode.OK, place)
        }

        // POST /food-places → Crear nuevo local
        post {
            val request = call.receive<FoodPlaceRequest>()

            foodPlaceService.create(request)
                .onSuccess { id ->
                    call.respond(
                        HttpStatusCode.Created,
                        MessageResponse("Local creado exitosamente con id: $id")
                    )
                }
                .onFailure { error ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        MessageResponse(error.message ?: "Error al crear", success = false)
                    )
                }
        }

        // PUT /food-places/{id} → Actualizar local existente
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    MessageResponse("ID inválido", success = false)
                )

            val request = call.receive<FoodPlaceRequest>()

            foodPlaceService.update(id, request)
                .onSuccess {
                    call.respond(HttpStatusCode.OK, MessageResponse("Local actualizado exitosamente"))
                }
                .onFailure { error ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        MessageResponse(error.message ?: "Error al actualizar", success = false)
                    )
                }
        }

        // DELETE /food-places/{id} → Eliminar local
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    MessageResponse("ID inválido", success = false)
                )

            foodPlaceService.delete(id)
                .onSuccess {
                    call.respond(HttpStatusCode.OK, MessageResponse("Local eliminado exitosamente"))
                }
                .onFailure { error ->
                    call.respond(
                        HttpStatusCode.NotFound,
                        MessageResponse(error.message ?: "Error al eliminar", success = false)
                    )
                }
        }
    }
}
