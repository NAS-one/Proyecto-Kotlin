package com.osornofoodroutes.presentation.ui.map

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.PolylineOptions
import com.osornofoodroutes.BuildConfig
import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.data.remote.RetrofitClient
import com.osornofoodroutes.presentation.theme.*
import com.osornofoodroutes.presentation.ui.home.getCategoryEmoji
import kotlinx.coroutines.launch
import android.util.Log

/**
 * Pantalla del Mapa con MapLibre (OpenStreetMap).
 * Muestra marcadores de todos los locales de comida en Osorno.
 * Usa OpenStreetMap como proveedor de tiles — 100% gratuito, sin API key.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    foodPlaces: List<FoodPlace>,
    initialFocusPlace: FoodPlace? = null,
    routePlaces: List<FoodPlace>? = null,
    onBack: () -> Unit
) {
    // Centro de Osorno, Chile
    val osornoCenter = LatLng(-40.5726, -73.1350)

    // Si hay un local focalizado, centrar en él
    val initialPosition = if (initialFocusPlace != null) {
        LatLng(initialFocusPlace.latitude, initialFocusPlace.longitude)
    } else {
        osornoCenter
    }

    var selectedPlace by remember { mutableStateOf<FoodPlace?>(null) }

    // Determinar qué locales mostrar
    val placesToShow = routePlaces ?: foodPlaces

    var realRoutePoints by remember { mutableStateOf<List<LatLng>?>(null) }

    // Llamada a la API de OpenRouteService
    LaunchedEffect(routePlaces) {
        if (routePlaces != null && routePlaces.size >= 2) {
            try {
                val allPoints = mutableListOf<LatLng>()
                // Calcular ruta entre puntos consecutivos
                for (i in 0 until routePlaces.size - 1) {
                    val start = routePlaces[i]
                    val end = routePlaces[i + 1]
                    val response = RetrofitClient.instance.getRoute(
                        apiKey = BuildConfig.ORS_API_KEY,
                        start = "${start.longitude},${start.latitude}",
                        end = "${end.longitude},${end.latitude}"
                    )
                    
                    val coordinates = response.features.firstOrNull()?.geometry?.coordinates
                    coordinates?.forEach { coord ->
                        if (coord.size >= 2) {
                            // GeoJSON devuelve [longitude, latitude], LatLng usa (latitude, longitude)
                            allPoints.add(LatLng(coord[1], coord[0]))
                        }
                    }
                }
                realRoutePoints = if (allPoints.isNotEmpty()) allPoints else null
            } catch (e: Exception) {
                Log.e("MapScreen", "Error fetching route from OpenRouteService", e)
                // Fallback a línea recta si la API falla o no hay Token
                realRoutePoints = routePlaces.map { LatLng(it.latitude, it.longitude) }
            }
        } else {
            realRoutePoints = null
        }
    }

    // Estilo de mapa OpenStreetMap gratuito (OpenFreeMap)
    val mapStyleUrl = "https://tiles.openfreemap.org/styles/liberty"

    // Inicializar MapLibre
    val context = LocalContext.current
    remember { MapLibre.getInstance(context) }

    // FIX: Gestión correcta del ciclo de vida del MapView
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapViewRef = remember { mutableStateOf<MapView?>(null) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            val mapView = mapViewRef.value ?: return@LifecycleEventObserver
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapViewRef.value?.onDestroy()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            if (routePlaces != null) "Ruta en Mapa" else "Mapa de Osorno",
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Text(
                            "${placesToShow.size} locales",
                            style = MaterialTheme.typography.bodySmall,
                            color = Taupe
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Charcoal)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Ivory
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // MapLibre Map (OpenStreetMap)
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        mapViewRef.value = this
                        getMapAsync { map ->
                            // Configurar estilo del mapa
                            map.setStyle(Style.Builder().fromUri(mapStyleUrl)) { style ->
                                // Posición inicial de la cámara
                                map.cameraPosition = CameraPosition.Builder()
                                    .target(initialPosition)
                                    .zoom(14.0)
                                    .build()

                                // Configurar controles del mapa
                                map.uiSettings.isZoomGesturesEnabled = true
                                map.uiSettings.isScrollGesturesEnabled = true
                                map.uiSettings.isRotateGesturesEnabled = true
                                map.uiSettings.isTiltGesturesEnabled = true
                                map.uiSettings.isCompassEnabled = true

                                // Listener para marcadores
                                map.setOnMarkerClickListener { marker ->
                                    selectedPlace = placesToShow.find {
                                        it.name == marker.title
                                    }
                                    true
                                }
                            }
                        }
                        // Gestión del ciclo de vida del MapView
                        onCreate(null)
                    }
                },
                update = { mapView ->
                    mapView.getMapAsync { map ->
                        // Limpiar marcadores y rutas anteriores
                        map.clear()
                        
                        // Agregar marcadores de cada local
                        placesToShow.forEach { place ->
                            val position = LatLng(place.latitude, place.longitude)
                            map.addMarker(
                                MarkerOptions()
                                    .position(position)
                                    .title(place.name)
                                    .snippet("${place.category} · ⭐ ${place.rating}")
                            )
                        }

                        // Dibujar línea de ruta real
                        realRoutePoints?.let { points ->
                            if (points.isNotEmpty()) {
                                map.addPolyline(
                                    PolylineOptions()
                                        .addAll(points)
                                        .color(AndroidColor.parseColor("#CC5A3A")) // Terracotta
                                        .width(5f)
                                )
                            }
                        }
                    }
                }
            )

            // Tarjeta del local seleccionado — diseño premium
            selectedPlace?.let { place ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Emoji de categoría
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TerracottaSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = getCategoryEmoji(place.category),
                                    fontSize = 22.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = place.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Charcoal
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = GoldStar,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "${place.rating}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = WarmBrown,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SageGreen.copy(alpha = 0.1f)
                                    ) {
                                        Text(
                                            place.category,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SageGreen,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                            IconButton(
                                onClick = { selectedPlace = null },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Cream)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    modifier = Modifier.size(18.dp),
                                    tint = WarmBrown
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Divider(color = Sand, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = place.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Taupe,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Taupe, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(place.address, style = MaterialTheme.typography.bodySmall, color = Taupe)
                        }
                        if (place.phone.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Taupe, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(place.phone, style = MaterialTheme.typography.bodySmall, color = Taupe)
                            }
                        }
                        if (place.openingHours.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = Taupe, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(place.openingHours, style = MaterialTheme.typography.bodySmall, color = Taupe)
                            }
                        }
                    }
                }
            }

            // Leyenda de categorías — diseño compacto
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite.copy(alpha = 0.96f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        "Categorías",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Charcoal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LegendItem("🍖 Restaurante")
                    LegendItem("☕ Café")
                    LegendItem("🍰 Pastelería")
                    LegendItem("🍔 Comida Rápida")
                    LegendItem("🏪 Mercado")
                    LegendItem("🍺 Bar")
                    LegendItem("🍲 Cocina Casera")
                    LegendItem("🧀 Emporio")
                }
            }
        }
    }
}

@Composable
private fun LegendItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = WarmBrown,
        modifier = Modifier.padding(vertical = 1.dp),
        fontSize = 11.sp
    )
}
