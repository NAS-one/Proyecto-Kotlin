package com.osornofoodroutes.presentation.ui.route

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.domain.model.Route
import com.osornofoodroutes.presentation.theme.*
import com.osornofoodroutes.presentation.ui.components.LoadingSpinner
import com.osornofoodroutes.presentation.viewmodel.RouteUiState

/**
 * Pantalla de lista de rutas del usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteListScreen(
    uiState: RouteUiState,
    allFoodPlaces: List<FoodPlace>,
    onCreateRoute: () -> Unit,
    onDeleteRoute: (Route) -> Unit,
    onViewRouteOnMap: (Route) -> Unit,
    onBack: () -> Unit
) {
    var routeToDelete by remember { mutableStateOf<Route?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mis Rutas", fontWeight = FontWeight.Bold, color = Charcoal)
                        Text(
                            "${uiState.routes.size} rutas creadas",
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateRoute,
                containerColor = SageGreen,
                contentColor = PureWhite,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear ruta")
            }
        },
        containerColor = Ivory
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                LoadingSpinner(color = SageGreen, size = 40.dp)
            }
        } else if (uiState.routes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(SageGreenLight.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🗺️", fontSize = 36.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No tienes rutas creadas",
                        style = MaterialTheme.typography.titleMedium,
                        color = Charcoal,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Crea tu primera ruta gastronómica",
                        style = MaterialTheme.typography.bodySmall,
                        color = Taupe
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onCreateRoute,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SageGreen,
                            contentColor = PureWhite
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Crear Ruta", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.routes) { route ->
                    RouteCard(
                        route = route,
                        foodPlaces = allFoodPlaces.filter { it.id in route.foodPlaceIds },
                        onViewOnMap = { onViewRouteOnMap(route) },
                        onDelete = { routeToDelete = route }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    // Diálogo de confirmación
    routeToDelete?.let { route ->
        AlertDialog(
            onDismissRequest = { routeToDelete = null },
            title = {
                Text("Eliminar Ruta", fontWeight = FontWeight.SemiBold)
            },
            text = {
                Text("¿Eliminar la ruta \"${route.name}\"? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteRoute(route)
                        routeToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorCoral,
                        contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { routeToDelete = null },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancelar")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun RouteCard(
    route: Route,
    foodPlaces: List<FoodPlace>,
    onViewOnMap: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SageGreen.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Route,
                        contentDescription = null,
                        tint = SageGreen,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = route.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Charcoal
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SageGreen.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "${route.foodPlaceIds.size} locales",
                                style = MaterialTheme.typography.bodySmall,
                                color = SageGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        if (route.estimatedTime.isNotBlank()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "· ${route.estimatedTime}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Taupe
                            )
                        }
                    }
                }
            }

            if (route.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = route.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Taupe,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            // Locales en la ruta con indicador numérico
            if (foodPlaces.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                foodPlaces.forEachIndexed { index, place ->
                    if (index < 3) {
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Número de parada
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Terracotta.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Terracotta,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = place.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = Charcoal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                if (foodPlaces.size > 3) {
                    Text(
                        text = "+${foodPlaces.size - 3} más",
                        style = MaterialTheme.typography.bodySmall,
                        color = Terracotta,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 30.dp, top = 2.dp)
                    )
                }
            }

            // Divider sutil
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Sand, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onViewOnMap) {
                    Icon(
                        Icons.Default.Map,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = SageGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ver en Mapa", color = SageGreen, style = MaterialTheme.typography.bodySmall)
                }
                TextButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = ErrorCoral
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar", color = ErrorCoral, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
