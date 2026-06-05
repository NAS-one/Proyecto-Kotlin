# 🍽️ Rutas de comida Osorno

Aplicación móvil Android desarrollada en **Kotlin** con **Jetpack Compose** para explorar rutas de locales de comida en la ciudad de **Osorno, Chile**.

## Capturas de Pantalla

La app incluye las siguientes pantallas:
- **Login / Registro** → Autenticación de usuarios
- **Home** → Dashboard con resumen de locales y rutas
- **Locales de Comida** → CRUD completo con filtro por categoría
- **Rutas** → Crear recorridos gastronómicos personalizados
- **Mapa** → Google Maps con marcadores y polilíneas de rutas

## Arquitectura

### Clean Architecture (3 Capas)
El proyecto implementa de forma estricta la **Clean Architecture** separando el código en 3 capas fundamentales, asegurando escalabilidad y mantenibilidad. Su estructura se ve así:

```text
com.osornofoodroutes
│
├── presentation/
│   ├── ui/ (screens & components)
│   ├── theme/
│   ├── navigation/
│   └── viewmodel/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
└── data/
    ├── remote/
    ├── local/
    └── repository/
```

### Capas en Detalle:
El proyecto implementa de forma estricta la **Clean Architecture** separando el código en 3 capas fundamentales, asegurando escalabilidad y mantenibilidad:

1. **Presentation (Capa de Presentación)**: 
   - Contiene la interfaz de usuario desarrollada con **Jetpack Compose** (`ui` / `theme`).
   - Manejo de estado y eventos con **ViewModels** (`viewmodel`).
   - Gestión de rutas y pantallas (`navigation`).

2. **Domain (Capa de Dominio)**:
   - Contiene la lógica de negocio central (independiente del framework de Android).
   - Entidades puras y modelos de datos (`model`).
   - Reglas de negocio a través de **Casos de Uso** (`usecase`).
   - Interfaces o contratos de los repositorios (`repository`).

3. **Data (Capa de Datos)**:
   - Implementa las interfaces de dominio y decide de dónde obtener los datos.
   - Acceso a base de datos local mediante **Room** (`local`).
   - Acceso a datos externos y APIs (`remote`).
   - Implementaciones concretas de los repositorios (`repository`).

### MVVM (Model-View-ViewModel)
- **Model**: Entidades de dominio (`User`, `FoodPlace`, `Route`)
- **View**: Composables (pantallas declarativas)
- **ViewModel**: Lógica de presentación con `StateFlow` (patrón Observer)

## Patrones de Diseño

| Patrón | Implementación |
|--------|---------------|
| **Repository** | Abstracción de acceso a datos Room |
| **Observer** | `StateFlow` en ViewModels → UI reactiva |
| **Factory** | `ViewModelProvider.Factory` para crear ViewModels |
| **Singleton** | Instancia única de `AppDatabase` (Room) |

## Principios SOLID

| Principio | Aplicación |
|-----------|-----------|
| **S** - Single Responsibility | Cada clase (ViewModel, UseCase, Repository) tiene una sola responsabilidad |
| **O** - Open/Closed | `FoodPlaceFormScreen` reutilizado para crear y editar sin modificación |
| **L** - Liskov Substitution | Las implementaciones de repositorios son intercambiables |
| **I** - Interface  | Interfaces separadas para `UserRepository`, `FoodPlaceRepository`, `RouteRepository` |
| **D** - Dependency Inversion | Los UseCases dependen de interfaces, no de implementaciones concretas |

## Tecnologías

- **Kotlin** 1.9+
- **Jetpack Compose** (Material 3)
- **Room Database** (persistencia local)
- **Navigation Compose**
- **Google Maps Compose** (maps-compose 4.3)
- **Coroutines + StateFlow** (asincronía reactiva)


## Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/Proyecto-Kotlin.git
```

### 2. Abrir en Android Studio
- Abrir el proyecto con Android Studio Hedgehog o superior
- Sincronizar Gradle
- Ejecutar en un emulador o dispositivo físico
- **Nota:** El mapa usa MapLibre y OpenFreeMap, por lo que **NO** requiere configuración de API Keys ni facturación. Funciona 100% gratis de forma predeterminada.

## 📍 Datos Pre-cargados

La app incluye 12 locales de comida reales de Osorno:
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

## 👤 Nicolás Almuna

Proyecto académico - Desarrollo de Aplicaciones Móviles con Kotlin
