package com.osornofoodroutes.presentation.ui.foodplace

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.osornofoodroutes.presentation.theme.*
import com.osornofoodroutes.presentation.ui.components.LoadingSpinner
import com.osornofoodroutes.presentation.ui.home.getCategoryEmoji
import com.osornofoodroutes.presentation.viewmodel.FoodPlaceUiState

/**
 * Pantalla de lista de locales de comida con filtro por categoría.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodPlaceListScreen(
    uiState: FoodPlaceUiState,
    filteredPlaces: List<FoodPlace>,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    onAddPlace: () -> Unit,
    onEditPlace: (FoodPlace) -> Unit,
    onDeletePlace: (FoodPlace) -> Unit,
    onViewOnMap: (FoodPlace) -> Unit,
    onBack: () -> Unit
) {
    var placeToDelete by remember { mutableStateOf<FoodPlace?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Locales de Comida",
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Text(
                            "${uiState.foodPlaces.size} locales registrados",
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
                onClick = onAddPlace,
                containerColor = Terracotta,
                contentColor = PureWhite,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar local")
            }
        },
        containerColor = Ivory
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filtro por categorías
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = uiState.selectedCategory == category,
                        onClick = { onCategorySelected(category) },
                        label = {
                            Text(
                                category,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (uiState.selectedCategory == category) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Terracotta,
                            selectedLabelColor = PureWhite,
                            containerColor = PureWhite,
                            labelColor = WarmBrown
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = uiState.selectedCategory == category,
                            borderColor = Sand,
                            selectedBorderColor = Terracotta
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            // Lista de locales
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingSpinner(color = Terracotta, size = 40.dp)
                }
            } else if (filteredPlaces.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(TerracottaSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🍽️", fontSize = 36.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No hay locales en esta categoría",
                            style = MaterialTheme.typography.titleMedium,
                            color = Charcoal,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Prueba con otra categoría o agrega uno nuevo",
                            style = MaterialTheme.typography.bodySmall,
                            color = Taupe
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredPlaces) { place ->
                        FoodPlaceCard(
                            place = place,
                            onEdit = { onEditPlace(place) },
                            onDelete = { placeToDelete = place },
                            onViewOnMap = { onViewOnMap(place) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    placeToDelete?.let { place ->
        AlertDialog(
            onDismissRequest = { placeToDelete = null },
            title = {
                Text("Eliminar Local", fontWeight = FontWeight.SemiBold)
            },
            text = {
                Text("¿Estás seguro de eliminar \"${place.name}\"? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeletePlace(place)
                        placeToDelete = null
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
                    onClick = { placeToDelete = null },
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text("Cancelar")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun FoodPlaceCard(
    place: FoodPlace,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewOnMap: () -> Unit
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
                verticalAlignment = Alignment.Top
            ) {
                // Icono de categoría
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(TerracottaSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getCategoryEmoji(place.category),
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Charcoal
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldStar,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${place.rating}",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmBrown,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SageGreen.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = place.category,
                                style = MaterialTheme.typography.bodySmall,
                                color = SageGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

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
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Taupe,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = place.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = Taupe,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (place.openingHours.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Taupe,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = place.openingHours,
                        style = MaterialTheme.typography.bodySmall,
                        color = Taupe
                    )
                }
            }

            // Divider sutil entre contenido y acciones
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Sand, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(4.dp))

            // Acciones
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
                    Text("Mapa", style = MaterialTheme.typography.bodySmall, color = SageGreen)
                }
                TextButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Terracotta
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar", style = MaterialTheme.typography.bodySmall, color = Terracotta)
                }
                TextButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = ErrorCoral
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Eliminar",
                        style = MaterialTheme.typography.bodySmall,
                        color = ErrorCoral
                    )
                }
            }
        }
    }
}
