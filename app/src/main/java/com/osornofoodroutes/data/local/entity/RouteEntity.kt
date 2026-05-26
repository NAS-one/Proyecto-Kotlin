package com.osornofoodroutes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para la tabla de rutas.
 * Los foodPlaceIds se almacenan como String separado por comas.
 */
@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val userId: Long,
    val foodPlaceIds: String,   // IDs separados por comas: "1,2,3"
    val estimatedTime: String = ""
)
