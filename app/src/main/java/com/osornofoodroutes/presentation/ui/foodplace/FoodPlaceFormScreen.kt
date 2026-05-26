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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.presentation.theme.*
import kotlin.text.padStart

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
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf("Lunes") }
    val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo", "Todos los días", "Lun-Vie")
    var startDay by remember { mutableStateOf("Lun") }
    var endDay by remember { mutableStateOf("Vie") }
    val daysList = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

// Estado para la hora de inicio (9:00 AM por defecto)
    val startTimeState = rememberTimePickerState(initialHour = 9, initialMinute = 0, is24Hour = true)

// Estado para la hora de fin (21:00 PM por defecto)
    val endTimeState = rememberTimePickerState(initialHour = 21, initialMinute = 0, is24Hour = true)

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
                onValueChange = { newValue ->
                    // Solo permite letras y espacios, máximo 40 caracteres
                    if (newValue.all { it.isLetter() || it.isWhitespace() } && newValue.length <= 40) {
                        name = newValue
                    }
                },

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
                label = { Text("Dejar breve descripcion y Redes Sociales") },
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
                onValueChange = { newValue ->
                    // Solo permite números y máximo 12 dígitos (ej: 56912345678)
                    if (newValue.all { it.isDigit() } && newValue.length <= 12) {
                        phone = newValue
                    }
                },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            // --- SECCIÓN DE HORARIO ---
            Text("Horario de atención", fontWeight = FontWeight.Bold, color = OrangePrimary)

            // Fila 1: Selección de Rango de Días
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Selector Día Inicial
                var expandedStart by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedStart,
                    onExpandedChange = { expandedStart = !expandedStart },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = startDay,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Desde") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStart) },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                    ExposedDropdownMenu(expanded = expandedStart, onDismissRequest = { expandedStart = false }) {
                        daysList.forEach { day ->
                            DropdownMenuItem(
                                text = { Text(day) },
                                onClick = { startDay = day; expandedStart = false }
                            )
                        }
                    }
                }

                // Selector Día Final
                var expandedEnd by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedEnd,
                    onExpandedChange = { expandedEnd = !expandedEnd },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = endDay,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Hasta") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEnd) },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                    ExposedDropdownMenu(expanded = expandedEnd, onDismissRequest = { expandedEnd = false }) {
                        daysList.forEach { day ->
                            DropdownMenuItem(
                                text = { Text(day) },
                                onClick = { endDay = day; expandedEnd = false }
                            )
                        }
                    }
                }
            } // Cierre de la Row de Días

            // Fila 2: Selección de Horas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showStartTimePicker = true },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Hora Inicio", style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Hora Fin", style = MaterialTheme.typography.labelSmall)
                }
            }

            // 4. Cuadro de visualización del resultado
            if (openingHours.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = OrangePrimary.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = OrangePrimary)
                        Spacer(Modifier.width(8.dp))
                        Text("Configurado: $openingHours", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                        IconButton(onClick = { openingHours = "" }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = androidx.compose.ui.graphics.Color.Gray)
                        }
                    }
                }
            }

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
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                enabled = name.isNotBlank() && address.isNotBlank()
            ) {
                Icon(if (isEditing) Icons.Default.Save else Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (isEditing) "Guardar Cambios" else "Agregar Local", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        } // Cierre del Column
    } // Cierre del Scaffold

    // --- DIÁLOGOS (DENTRO DE LA FUNCIÓN, FUERA DEL SCAFFOLD) ---

    if (showStartTimePicker) {
        AlertDialog(
            onDismissRequest = { showStartTimePicker = false },
            confirmButton = { TextButton(onClick = { showStartTimePicker = false }) { Text("OK") } },
            text = {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text("Hora de Apertura", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
                    TimePicker(state = startTimeState)
                }
            }
        )
    }

    if (showEndTimePicker) {
        AlertDialog(
            onDismissRequest = { showEndTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val startH = startTimeState.hour.toString().padStart(2, '0')
                    val startM = startTimeState.minute.toString().padStart(2, '0')
                    val endH = endTimeState.hour.toString().padStart(2, '0')
                    val endM = endTimeState.minute.toString().padStart(2, '0')

                    // Formato final: "Lun-Vie 09:00 - 21:00"
                    openingHours = "$startDay-$endDay $startH:$startM - $endH:$endM"
                    showEndTimePicker = false
                }) { Text("Confirmar Horario") }
            },
            text = {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text("Hora de Cierre", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
                    TimePicker(state = endTimeState)
                }
            }
        )
    }
}


