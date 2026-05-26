package com.osornofoodroutes.data.local

import com.osornofoodroutes.data.local.entity.FoodPlaceEntity

/**
 * Datos precargados de locales de comida en Osorno.
 * Coordenadas reales de la ciudad de Osorno, Región de Los Lagos, Chile.
 */
object FoodPlaceSeeder {

    fun getInitialFoodPlaces(): List<FoodPlaceEntity> = listOf(
        FoodPlaceEntity(
            name = "Mercado Municipal de Osorno",
            description = "Mercado tradicional con puestos de comida típica sureña, mariscos frescos y productos locales.",
            category = "Mercado",
            address = "Ramírez 1000, Osorno",
            latitude = -40.5726,
            longitude = -73.1350,
            rating = 4.3f,
            phone = "+56 64 231 0000",
            openingHours = "Lun-Sáb 08:00-18:00"
        ),
        FoodPlaceEntity(
            name = "Café Haussman",
            description = "Café tradicional alemán con kuchen artesanal y pastelería de la zona.",
            category = "Café",
            address = "O'Higgins 743, Osorno",
            latitude = -40.5740,
            longitude = -73.1320,
            rating = 4.5f,
            phone = "+56 64 223 4567",
            openingHours = "Lun-Vie 09:00-20:00, Sáb 10:00-18:00"
        ),
        FoodPlaceEntity(
            name = "La Parrilla de Toño",
            description = "Parrillada al estilo sureño con carnes a la brasa y acompañamientos criollos.",
            category = "Restaurante",
            address = "Manuel Rodríguez 1050, Osorno",
            latitude = -40.5710,
            longitude = -73.1290,
            rating = 4.2f,
            phone = "+56 64 225 8901",
            openingHours = "Lun-Dom 12:00-23:00"
        ),
        FoodPlaceEntity(
            name = "Restaurante Bavaria",
            description = "Restaurante de comida alemana y chilena con cerveza artesanal local.",
            category = "Restaurante",
            address = "O'Higgins 911, Osorno",
            latitude = -40.5745,
            longitude = -73.1340,
            rating = 4.4f,
            phone = "+56 64 224 5678",
            openingHours = "Mar-Dom 12:00-22:00"
        ),
        FoodPlaceEntity(
            name = "El Fogón Sureño",
            description = "Asados y comida típica del sur de Chile. Especialidad en cordero al palo.",
            category = "Restaurante",
            address = "Av. Juan Mackenna 1085, Osorno",
            latitude = -40.5755,
            longitude = -73.1310,
            rating = 4.6f,
            phone = "+56 64 226 7890",
            openingHours = "Mié-Dom 12:00-22:00"
        ),
        FoodPlaceEntity(
            name = "Donde la Negra",
            description = "Cocina casera sureña con platos del día abundantes y económicos.",
            category = "Cocina Casera",
            address = "Freire 530, Osorno",
            latitude = -40.5732,
            longitude = -73.1360,
            rating = 4.1f,
            phone = "+56 64 227 1234",
            openingHours = "Lun-Vie 12:00-16:00"
        ),
        FoodPlaceEntity(
            name = "Café Central",
            description = "Café con ambiente acogedor, sándwiches artesanales y postres caseros.",
            category = "Café",
            address = "Ramírez 925, Osorno",
            latitude = -40.5738,
            longitude = -73.1335,
            rating = 4.0f,
            phone = "+56 64 228 5678",
            openingHours = "Lun-Sáb 08:30-21:00"
        ),
        FoodPlaceEntity(
            name = "Fuente Alemana Osorno",
            description = "Sándwiches enormes estilo fuente de soda, lomitos y churrascos clásicos.",
            category = "Comida Rápida",
            address = "Eleuterio Ramírez 898, Osorno",
            latitude = -40.5722,
            longitude = -73.1345,
            rating = 4.3f,
            phone = "+56 64 229 0123",
            openingHours = "Lun-Sáb 10:00-22:00"
        ),
        FoodPlaceEntity(
            name = "Dino's Pizza",
            description = "Pizzería artesanal con ingredientes frescos y masa hecha en el local.",
            category = "Comida Rápida",
            address = "Patricio Lynch 1457, Osorno",
            latitude = -40.5768,
            longitude = -73.1300,
            rating = 4.2f,
            phone = "+56 64 230 4567",
            openingHours = "Lun-Dom 11:00-23:00"
        ),
        FoodPlaceEntity(
            name = "Pastelería Maestranza",
            description = "Pastelería fina y panadería con tradición alemana. Kuchen y tortas para toda ocasión.",
            category = "Pastelería",
            address = "Cochrane 602, Osorno",
            latitude = -40.5715,
            longitude = -73.1325,
            rating = 4.7f,
            phone = "+56 64 231 8901",
            openingHours = "Lun-Sáb 08:00-20:00"
        ),
        FoodPlaceEntity(
            name = "Rincón Cervecero",
            description = "Bar y restaurante con cervezas artesanales de la región y picoteo sureño.",
            category = "Bar",
            address = "Av. República 550, Osorno",
            latitude = -40.5760,
            longitude = -73.1370,
            rating = 4.1f,
            phone = "+56 64 232 2345",
            openingHours = "Jue-Sáb 18:00-01:00"
        ),
        FoodPlaceEntity(
            name = "Emporio Sureño",
            description = "Tienda gourmet con productos locales, quesos, mermeladas y comida preparada.",
            category = "Emporio",
            address = "Manuel Antonio Matta 780, Osorno",
            latitude = -40.5700,
            longitude = -73.1315,
            rating = 4.4f,
            phone = "+56 64 233 6789",
            openingHours = "Lun-Vie 09:00-19:00, Sáb 10:00-14:00"
        )
    )
}
