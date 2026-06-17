package com.osornofoodroutes.presentation.ui.foodplace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

    var name by remember { mutableStateOf(existingPlace?.name ?: "") }
    var description by remember { mutableStateOf(existingPlace?.description ?: "") }
    var category by remember { mutableStateOf(existingPlace?.category ?: "Restaurante") }
    var address by remember { mutableStateOf(existingPlace?.address ?: "") }
    var latitude by remember { mutableStateOf(existingPlace?.latitude?.toString() ?: "-40.5726") }
    var longitude by remember { mutableStateOf(existingPlace?.longitude?.toString() ?: "-73.1350") }
    var rating by remember { mutableStateOf(existingPlace?.rating?.toString() ?: "4.0") }
    var phone by remember { mutableStateOf(existingPlace?.phone ?: "") }
    var openingHours by remember { mutableStateOf(existingPlace?.openingHours ?: "") }

    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Restaurante", "Café", "Pastelería", "Comida Rápida", "Mercado", "Bar", "Cocina Casera", "Emporio")

    var expandedRating by remember { mutableStateOf(false) }
    val ratings = listOf("5.0", "4.0", "3.0", "2.0", "1.0")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Editar Local" else "Nuevo Local",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CreamBackground
                )
            )
        },
        containerColor = CreamBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del local *") },
                leadingIcon = { Icon(Icons.Default.Restaurant, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
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
                    leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
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
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 2,
                maxLines = 4
            )

            // Dirección
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección *") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
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
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitud") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            // Rating (Dropdown para evitar valores incorrectos)
            ExposedDropdownMenuBox(
                expanded = expandedRating,
                onExpandedChange = { expandedRating = !expandedRating }
            ) {
                OutlinedTextField(
                    value = "$rating Estrellas",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rating") },
                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = OrangePrimary) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRating) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
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

            // Teléfono
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            // Horario
            OutlinedTextField(
                value = openingHours,
                onValueChange = { openingHours = it },
                label = { Text("Horario de atención") },
                leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                placeholder = { Text("Ej: Lun-Vie 09:00-21:00") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Guardar
            Button(
                onClick = {
                    val foodPlace = FoodPlace(
                        id = existingPlace?.id ?: 0,
                        name = name,
                        description = description,
                        category = category,
                        address = address,
                        latitude = latitude.toDoubleOrNull() ?: -40.5726,
                        longitude = longitude.toDoubleOrNull() ?: -73.1350,
                        rating = rating.toFloatOrNull() ?: 0f,
                        phone = phone,
                        openingHours = openingHours
                    )
                    onSave(foodPlace)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
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
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
