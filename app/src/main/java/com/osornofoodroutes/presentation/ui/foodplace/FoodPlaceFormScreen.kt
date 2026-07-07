package com.osornofoodroutes.presentation.ui.foodplace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.presentation.theme.*

/**
 * Formulario para agregar o editar un local de comida.
 * Reutilizado para Create y Update (Principio OCP).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodPlaceFormScreen(
    existingPlace: FoodPlace? = null,
    onSave: (FoodPlace) -> Unit,
    onBack: () -> Unit
) {
    val isEditing = existingPlace != null
    val focusManager = LocalFocusManager.current

    // FIX: Usar existingPlace como key para que los campos se actualicen correctamente
    var name by remember(existingPlace) { mutableStateOf(existingPlace?.name ?: "") }
    var description by remember(existingPlace) { mutableStateOf(existingPlace?.description ?: "") }
    var category by remember(existingPlace) { mutableStateOf(existingPlace?.category ?: "Restaurante") }
    var address by remember(existingPlace) { mutableStateOf(existingPlace?.address ?: "") }
    var latitude by remember(existingPlace) { mutableStateOf(existingPlace?.latitude?.toString() ?: "-40.5726") }
    var longitude by remember(existingPlace) { mutableStateOf(existingPlace?.longitude?.toString() ?: "-73.1350") }
    var rating by remember(existingPlace) { mutableStateOf(existingPlace?.rating?.toString() ?: "4.0") }
    var phone by remember(existingPlace) { mutableStateOf(existingPlace?.phone ?: "") }
    var openingHours by remember(existingPlace) { mutableStateOf(existingPlace?.openingHours ?: "") }

    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Restaurante", "Café", "Pastelería", "Comida Rápida", "Mercado", "Bar", "Cocina Casera", "Emporio")

    var expandedRating by remember { mutableStateOf(false) }
    val ratings = listOf("5.0", "4.0", "3.0", "2.0", "1.0")

    // Colores reutilizables para OutlinedTextField
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Terracotta,
        unfocusedBorderColor = Sand,
        focusedLabelColor = Terracotta,
        cursorColor = Terracotta
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            if (isEditing) "Editar Local" else "Nuevo Local",
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Text(
                            if (isEditing) "Modifica los datos del local" else "Completa la información",
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
        containerColor = Ivory
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Sección: Información Básica
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
                        "Información Básica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Charcoal
                    )

                    // Nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre del local *") },
                        leadingIcon = { Icon(Icons.Default.Restaurant, contentDescription = null, tint = Taupe) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = textFieldColors,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Categoría (Dropdown)
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = Taupe) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(14.dp),
                            colors = textFieldColors
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        category = cat
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Descripción
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = Taupe) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        minLines = 2,
                        maxLines = 4,
                        colors = textFieldColors
                    )

                    // Rating (Dropdown)
                    ExposedDropdownMenuBox(
                        expanded = expandedRating,
                        onExpandedChange = { expandedRating = !expandedRating }
                    ) {
                        OutlinedTextField(
                            value = "$rating Estrellas",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Rating") },
                            leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = GoldStar) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRating) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(14.dp),
                            colors = textFieldColors
                        )
                        ExposedDropdownMenu(
                            expanded = expandedRating,
                            onDismissRequest = { expandedRating = false }
                        ) {
                            ratings.forEach { r ->
                                DropdownMenuItem(
                                    text = { Text("$r Estrellas") },
                                    onClick = {
                                        rating = r
                                        expandedRating = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección: Ubicación
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
                        "Ubicación",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Charcoal
                    )

                    // Dirección
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Dirección *") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Taupe) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = textFieldColors,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Coordenadas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = latitude,
                            onValueChange = { latitude = it },
                            label = { Text("Latitud") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Right) }
                            ),
                            singleLine = true,
                            colors = textFieldColors
                        )
                        OutlinedTextField(
                            value = longitude,
                            onValueChange = { longitude = it },
                            label = { Text("Longitud") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            colors = textFieldColors
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección: Detalles de Contacto
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
                        "Detalles de Contacto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Charcoal
                    )

                    // Teléfono
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Teléfono") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Taupe) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        colors = textFieldColors
                    )

                    // Horario
                    OutlinedTextField(
                        value = openingHours,
                        onValueChange = { openingHours = it },
                        label = { Text("Horario de atención") },
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = Taupe) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        placeholder = { Text("Ej: Lun-Vie 09:00-21:00") },
                        colors = textFieldColors,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Guardar
            Button(
                onClick = {
                    focusManager.clearFocus()
                    val foodPlace = FoodPlace(
                        id = existingPlace?.id ?: 0,
                        name = name.trim(),
                        description = description.trim(),
                        category = category,
                        address = address.trim(),
                        latitude = latitude.toDoubleOrNull() ?: -40.5726,
                        longitude = longitude.toDoubleOrNull() ?: -73.1350,
                        rating = rating.toFloatOrNull() ?: 0f,
                        phone = phone.trim(),
                        openingHours = openingHours.trim()
                    )
                    onSave(foodPlace)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terracotta,
                    contentColor = PureWhite
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                enabled = name.isNotBlank() && address.isNotBlank()
            ) {
                Icon(
                    if (isEditing) Icons.Default.Save else Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isEditing) "Guardar Cambios" else "Agregar Local",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
