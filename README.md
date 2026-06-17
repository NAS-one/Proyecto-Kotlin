# 🍽️ Osorno Food Routes

Aplicación móvil Android desarrollada en **Kotlin** con **Jetpack Compose** para explorar rutas de locales de comida en la ciudad de **Osorno, Chile**. Incluye un **backend propio** desarrollado con **Ktor** conectado a una base de datos **PostgreSQL en Neon**.

## Pantallas de la App

- **Login / Registro** → Autenticación de usuarios con JWT
- **Home** → Dashboard con resumen de locales y rutas
- **Locales de Comida** → CRUD completo con filtro por categoría
- **Rutas** → Crear recorridos gastronómicos personalizados
- **Mapa** → Visualización con MapLibre y trazado de rutas

---

## Arquitectura General

El proyecto es un **monorepo Gradle multi-módulo** compuesto por dos módulos:

| Módulo | Tecnología | Función |
|--------|-----------|---------|
| `:app` | Android + Jetpack Compose | Aplicación móvil (cliente) |
| `:backend` | Ktor + Exposed + PostgreSQL | Servidor REST API |

```text
Proyecto-Kotlin/
├── app/           → Aplicación Android (Clean Architecture + MVVM)
├── backend/       → Servidor Ktor (API REST + JWT + PostgreSQL)
├── settings.gradle.kts  → include(":app", ":backend")
└── build.gradle.kts     → Configuración raíz
```

---

## 🖥️ Backend (Ktor Server)

### Stack Tecnológico

| Componente | Tecnología | Versión |
|-----------|-----------|---------|
| **Framework** | Ktor Server (Netty) | 2.3.7 |
| **ORM** | Exposed (Jetbrains) | 0.45.0 |
| **Base de Datos** | PostgreSQL (Neon Serverless) | — |
| **Pool de Conexiones** | HikariCP | 5.1.0 |
| **Autenticación** | JWT (HMAC256) | — |
| **Hashing** | BCrypt | 0.10.2 |
| **Serialización** | Kotlinx Serialization (JSON) | — |
| **Logging** | Logback | 1.4.14 |

### Arquitectura del Backend (Capas)

```text
com.osornofoodroutes.backend
│
├── Application.kt             → Punto de entrada, configuración del servidor
│
├── plugins/
│   ├── DatabaseFactory.kt     → Conexión a Neon PostgreSQL + HikariCP
│   ├── Security.kt            → Configuración JWT (verificación de tokens)
│   └── Serialization.kt       → Content Negotiation (JSON)
│
├── tables/                    → Definición de tablas (Exposed ORM)
│   ├── UsersTable.kt          → Tabla users (id, name, email, password_hash)
│   ├── FoodPlacesTable.kt     → Tabla food_places (10 columnas)
│   └── RoutesTable.kt         → Tabla routes (con FK a users)
│
├── dto/                       → Data Transfer Objects (request/response)
│   ├── AuthDTO.kt             → RegisterRequest, LoginRequest, TokenResponse, MessageResponse
│   ├── FoodPlaceDTO.kt        → FoodPlaceRequest, FoodPlaceResponse
│   └── RouteDTO.kt            → RouteRequest, RouteResponse
│
├── repository/                → Acceso directo a la base de datos (SQL)
│   ├── UserRepository.kt      → CRUD usuarios (insert, findByEmail, findById)
│   ├── FoodPlaceRepository.kt → CRUD locales (insert, getAll, getById, getByCategory, update, delete)
│   └── RouteRepository.kt     → CRUD rutas (insert, getByUserId, getAll, delete)
│
├── service/                   → Lógica de negocio + validaciones
│   ├── UserService.kt         → Registro (BCrypt hash) + Login (genera JWT 24h)
│   ├── FoodPlaceService.kt    → Validaciones de CRUD (nombre, dirección, rating 0-5)
│   └── RouteService.kt        → Validaciones (mín. 2 locales, ownership en delete)
│
└── routes/                    → Endpoints HTTP (API REST)
    ├── AuthRoutes.kt          → POST /auth/register, POST /auth/login
    ├── FoodPlaceRoutes.kt     → GET/POST/PUT/DELETE /food-places
    └── RouteRoutes.kt         → GET/POST/DELETE /routes (protegido con JWT)
```

### Endpoints de la API

| Método | Endpoint | Autenticación | Descripción |
|--------|----------|:------------:|-------------|
| `POST` | `/auth/register` | ❌ | Registrar nuevo usuario |
| `POST` | `/auth/login` | ❌ | Login → devuelve token JWT |
| `GET` | `/food-places` | ❌ | Listar todos (acepta `?category=`) |
| `GET` | `/food-places/{id}` | ❌ | Obtener un local por ID |
| `POST` | `/food-places` | ❌ | Crear nuevo local |
| `PUT` | `/food-places/{id}` | ❌ | Actualizar local existente |
| `DELETE` | `/food-places/{id}` | ❌ | Eliminar local |
| `GET` | `/routes` | ✅ JWT | Listar rutas del usuario autenticado |
| `GET` | `/routes/all` | ✅ JWT | Listar todas las rutas (admin/debug) |
| `POST` | `/routes` | ✅ JWT | Crear nueva ruta |
| `DELETE` | `/routes/{id}` | ✅ JWT | Eliminar ruta (solo el dueño) |

### Flujo de Autenticación

```text
1. Cliente envía POST /auth/register → { name, email, password }
2. Servidor hashea password con BCrypt y guarda en PostgreSQL
3. Cliente envía POST /auth/login → { email, password }
4. Servidor verifica BCrypt, genera JWT (HMAC256, 24h) con claims userId + email
5. Cliente incluye el token en rutas protegidas: Authorization: Bearer <token>
6. Servidor verifica el JWT en cada request protegida
```

### Configuración del Servidor

El backend se configura mediante `application.conf` en `backend/src/main/resources/`:
- **Puerto**: 8080 (configurable)
- **CORS**: Habilitado para todos los hosts
- **StatusPages**: Manejo global de errores con respuestas JSON
- **Base de datos**: Neon PostgreSQL (serverless) con auto-creación de tablas

---

## 📱 App Android (Cliente)

### Clean Architecture (3 Capas)

```text
com.osornofoodroutes
│
├── OsornoFoodRoutesApp.kt           → Application class
│
├── presentation/                     → Capa de Presentación
│   ├── MainActivity.kt              → Entry point de la app
│   ├── navigation/
│   │   ├── Screen.kt                → Sealed class con destinos de navegación
│   │   └── AppNavigation.kt         → NavHost y configuración de rutas
│   ├── theme/                        → Material 3 Theme (colores, tipografía)
│   ├── ui/
│   │   ├── auth/                     → Pantallas de Login y Registro
│   │   ├── home/                     → Dashboard principal
│   │   ├── foodplace/                → CRUD de locales de comida
│   │   ├── route/                    → Gestión de rutas gastronómicas
│   │   └── map/                      → Mapa con MapLibre
│   └── viewmodel/
│       ├── AuthViewModel.kt          → Estado de autenticación
│       ├── FoodPlaceViewModel.kt     → CRUD + filtros de locales
│       └── RouteViewModel.kt         → Gestión de rutas
│
├── domain/                           → Capa de Dominio (reglas de negocio)
│   ├── model/
│   │   ├── User.kt                   → Entidad de usuario
│   │   ├── FoodPlace.kt              → Entidad de local de comida
│   │   └── Route.kt                  → Entidad de ruta gastronómica
│   ├── repository/
│   │   ├── UserRepository.kt         → Interface del repositorio de usuarios
│   │   ├── FoodPlaceRepository.kt    → Interface del repositorio de locales
│   │   └── RouteRepository.kt        → Interface del repositorio de rutas
│   └── usecase/
│       ├── auth/                      → Casos de uso de autenticación
│       ├── foodplace/                 → Casos de uso de locales
│       └── route/                     → Casos de uso de rutas
│
└── data/                             → Capa de Datos (implementaciones)
    ├── local/
    │   ├── AppDatabase.kt            → Room Database (SQLite local)
    │   ├── FoodPlaceSeeder.kt        → Pre-carga de 12 locales reales de Osorno
    │   ├── Mappers.kt                → Conversión Entity ↔ Domain Model
    │   ├── dao/                      → Data Access Objects (Room)
    │   └── entity/                   → Entities de Room (@Entity)
    ├── remote/
    │   └── OpenRouteApiService.kt    → Retrofit API para trazado de rutas
    └── repository/
        ├── UserRepositoryImpl.kt     → Implementación con Room
        ├── FoodPlaceRepositoryImpl.kt → Implementación con Room
        └── RouteRepositoryImpl.kt    → Implementación con Room
```

### Patrón MVVM (Model-View-ViewModel)

| Componente | Implementación |
|-----------|---------------|
| **Model** | Entidades de dominio (`User`, `FoodPlace`, `Route`) |
| **View** | Composables declarativos (Jetpack Compose + Material 3) |
| **ViewModel** | `StateFlow` para estado reactivo + Coroutines para operaciones async |

---

## Patrones de Diseño

| Patrón | Implementación |
|--------|---------------|
| **Repository** | Interfaces en `domain/repository` → Implementaciones en `data/repository` (Room local) y `backend/repository` (Exposed SQL) |
| **Service Layer** | Servicios en el backend (`UserService`, `FoodPlaceService`, `RouteService`) con validaciones y lógica de negocio |
| **DTO** | Objetos de transferencia separados para request y response en la API |
| **Observer** | `StateFlow` en ViewModels → UI reactiva en Compose |
| **Factory** | `ViewModelProvider.Factory` para inyección de dependencias en ViewModels |
| **Singleton** | `AppDatabase` (Room), `DatabaseFactory` (Exposed/HikariCP) |

## Principios SOLID

| Principio | Aplicación |
|-----------|-----------| 
| **S** - Single Responsibility | Cada clase tiene una sola responsabilidad: Repository (datos), Service (negocio), Route (HTTP), ViewModel (UI state) |
| **O** - Open/Closed | `FoodPlaceFormScreen` reutilizado para crear y editar sin modificación |
| **L** - Liskov Substitution | Las implementaciones de repositorios son intercambiables con sus interfaces |
| **I** - Interface Segregation | Interfaces separadas: `UserRepository`, `FoodPlaceRepository`, `RouteRepository` |
| **D** - Dependency Inversion | Los Services y UseCases dependen de interfaces, no de implementaciones concretas |

---

## Tecnologías

### App Android
- **Kotlin** 1.9+ (JVM 17)
- **Jetpack Compose** (Material 3)
- **Room Database** (persistencia local SQLite)
- **Navigation Compose** (navegación entre pantallas)
- **MapLibre** 11.5.2 (mapas OpenStreetMap)
- **Retrofit** 2.9.0 + Gson (consumo de APIs)
- **Coroutines** + StateFlow (asincronía reactiva)
- **KSP** (procesador de anotaciones para Room)

### Backend
- **Ktor Server** 2.3.7 (Netty)
- **Exposed ORM** 0.45.0 (consultas SQL type-safe)
- **PostgreSQL** (Neon Serverless Cloud)
- **HikariCP** 5.1.0 (pool de conexiones)
- **JWT** + BCrypt (autenticación segura)
- **Kotlinx Serialization** (JSON)
- **Logback** 1.4.14 (logging)

---

## Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/Proyecto-Kotlin.git
```

### 2. Configurar el Backend
El backend requiere una base de datos PostgreSQL. La configuración está en:
```
backend/src/main/resources/application.conf
```

Para ejecutar el backend:
```bash
./gradlew :backend:run
```
El servidor se inicia en `http://localhost:8080`.

### 3. Abrir la App en Android Studio
- Abrir el proyecto con **Android Studio Hedgehog** o superior
- Sincronizar Gradle
- Ejecutar el módulo `:app` en un emulador o dispositivo físico
- **Nota:** El mapa usa MapLibre y OpenFreeMap, por lo que **NO** requiere API Keys ni facturación para el mapa base

### 4. API Key de OpenRouteService (Opcional)
Para el trazado de rutas en el mapa, configurar en `secrets.properties`:
```properties
ORS_API_KEY=tu_api_key_aqui
```

---

## 📍 Datos Pre-cargados

La app incluye 11 locales de comida reales de Osorno pre-cargados via `FoodPlaceSeeder`:
- Mercado Municipal de Osorno
- Café Haussman
- La Parrilla de Toño
- Restaurante Bavaria
- El Fogón Sureño
- Donde la Negra
- Café Central
- Fuente Alemana Osorno
- Pastelería Maestranza
- Rincón Cervecero
- Emporio Sureño

---

## Nicolás Almuna

Proyecto académico - Desarrollo de Aplicaciones Móviles con Kotlin
