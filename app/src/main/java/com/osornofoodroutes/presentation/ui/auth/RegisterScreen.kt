package com.osornofoodroutes.presentation.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osornofoodroutes.presentation.theme.*
import com.osornofoodroutes.presentation.viewmodel.AuthUiState

/**
 * Pantalla de registro de usuario.
 * Diseño consistente con LoginScreen — fondo decorativo con clip redondeado.
 */
@Composable
fun RegisterScreen(
    uiState: AuthUiState,
    onRegister: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onClearError: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    // Animación de aparición
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ivory)
    ) {
        // Fondo decorativo superior — consistente con LoginScreen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(SageGreen, SageGreenLight)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Título
            Text(
                text = "📝",
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineLarge,
                color = PureWhite,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Únete y descubre rutas gastronómicas",
                style = MaterialTheme.typography.bodyMedium,
                color = PureWhite.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Card de Registro con animación
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Nombre
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it; onClearError(); localError = null },
                            label = { Text("Nombre completo") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Taupe)
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SageGreen,
                                unfocusedBorderColor = Sand,
                                focusedLabelColor = SageGreen,
                                cursorColor = SageGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; onClearError(); localError = null },
                            label = { Text("Email") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = Taupe)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SageGreen,
                                unfocusedBorderColor = Sand,
                                focusedLabelColor = SageGreen,
                                cursorColor = SageGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Contraseña
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; onClearError(); localError = null },
                            label = { Text("Contraseña") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Taupe)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.Visibility
                                        else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password",
                                        tint = Taupe
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SageGreen,
                                unfocusedBorderColor = Sand,
                                focusedLabelColor = SageGreen,
                                cursorColor = SageGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Confirmar Contraseña
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it; localError = null },
                            label = { Text("Confirmar contraseña") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Taupe)
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SageGreen,
                                unfocusedBorderColor = Sand,
                                focusedLabelColor = SageGreen,
                                cursorColor = SageGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Errores
                        AnimatedVisibility(visible = uiState.errorMessage != null || localError != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = ErrorCoral.copy(alpha = 0.1f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = localError ?: uiState.errorMessage ?: "",
                                    color = ErrorCoral,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Botón Registrar
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                when {
                                    name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                                        localError = "Completa todos los campos"
                                    }
                                    !email.contains("@") || !email.contains(".") -> {
                                        localError = "Ingresa un email válido"
                                    }
                                    password.length < 4 -> {
                                        localError = "La contraseña debe tener al menos 4 caracteres"
                                    }
                                    password != confirmPassword -> {
                                        localError = "Las contraseñas no coinciden"
                                    }
                                    else -> {
                                        localError = null
                                        onRegister(name.trim(), email.trim(), password)
                                    }
                                }
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
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = PureWhite,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Registrarse",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(onClick = onNavigateToLogin) {
                            Text(
                                "¿Ya tienes cuenta? ",
                                color = Taupe,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Inicia sesión",
                                color = SageGreen,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
