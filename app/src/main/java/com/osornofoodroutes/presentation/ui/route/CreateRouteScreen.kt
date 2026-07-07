package com.osornofoodroutes.presentation.ui.route

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.domain.model.Route
import com.osornofoodroutes.presentation.theme.*
import com.osornofoodroutes.presentation.ui.home.getCategoryEmoji

/**
 * Formulario para crear una nueva ruta seleccionando locales.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRouteScreen(
    allFoodPlaces: List<FoodPlace>,
    userId: Long,
    onSave: (Route) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var estimatedTime by remember { mutableStateOf("") }
    var selectedPlaceIds by remember { mutableStateOf(setOf<Long>()) }

    // Colores reutilizables para OutlinedTextField
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = SageGreen,
        unfocusedBorderColor = Sand,
        focusedLabelColor = SageGreen,
        cursorColor = SageGreen
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Nueva Ruta", fontWeight = FontWeight.Bold, color = Charcoal)
                        Text(
                            "Selecciona locales para tu recorrido",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Ivory)
            )
        },
        containerColor = Ivory
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Formulario en Card
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            "Datos de la Ruta",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Charcoal
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre de la ruta *") },
                            leadingIcon = { Icon(Icons.Default.Route, contentDescription = null, tint = Taupe) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = textFieldColors
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Descripción") },
                            leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = Taupe) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            minLines = 2,
                            colors = textFieldColors
                        )

                        OutlinedTextField(
                            value = estimatedTime,
                            onValueChange = { estimatedTime = it },
                            label = { Text("Tiempo estimado") },
                            leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = Taupe) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            placeholder = { Text("Ej: 2 horas") },
                            colors = textFieldColors
                        )
                    }
                }
            }

            // Header selección de locales con badge
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Selecciona los locales",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Charcoal,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (selectedPlaceIds.isNotEmpty()) SageGreen else Taupe.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "${selectedPlaceIds.size} seleccionados",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selectedPlaceIds.isNotEmpty()) PureWhite else Taupe,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Lista de locales seleccionables
            items(allFoodPlaces) { place ->
                val isSelected = place.id in selectedPlaceIds
                val cardColor by animateColorAsState(
                    targetValue = if (isSelected) SageGreen.copy(alpha = 0.08f) else PureWhite,
                    label = "cardBg"
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isSelected) 3.dp else 1.dp
                    ),
                    border = if (isSelected) {
                        androidx.compose.foundation.BorderStroke(
                            width = 1.5.dp,
                            color = SageGreen
                        )
                    } else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { checked ->
                                selectedPlaceIds = if (checked) {
                                    selectedPlaceIds + place.id
                                } else {
                                    selectedPlaceIds - place.id
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = SageGreen,
                                uncheckedColor = Sand
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        // Emoji de categoría
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(TerracottaSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getCategoryEmoji(place.category),
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = place.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                color = Charcoal
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = place.category,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Taupe
                                )
                                Text(
                                    text = " · ",
                                    color = Taupe,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldStar,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${place.rating}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WarmBrown,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Indicador de selección
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(SageGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = PureWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val route = Route(
                            name = name.trim(),
                            description = description.trim(),
                            userId = userId,
                            foodPlaceIds = selectedPlaceIds.toList(),
                            estimatedTime = estimatedTime.trim()
                        )
                        onSave(route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SageGreen,
                        contentColor = PureWhite
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    enabled = name.isNotBlank() && selectedPlaceIds.isNotEmpty()
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Crear Ruta",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}
