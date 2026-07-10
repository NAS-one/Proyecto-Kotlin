package com.osornofoodroutes.presentation.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.osornofoodroutes.presentation.ui.components.LoadingSpinner
import com.osornofoodroutes.presentation.viewmodel.AuthUiState

/**
 * Pantalla de inicio de sesión.
 * Diseño elegante y minimalista con tonos tierra.
 */
@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onClearError: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    // Animación eliminada por estabilidad

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ivory)
    ) {
        // Fondo decorativo superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Terracotta, TerracottaLight)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo / Título
            Text(
                text = "🍽️",
                fontSize = 56.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Osorno",
                style = MaterialTheme.typography.headlineLarge,
                color = PureWhite,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "FOOD ROUTES",
                style = MaterialTheme.typography.titleMedium,
                color = PureWhite.copy(alpha = 0.85f),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Descubre los sabores de la ciudad",
                style = MaterialTheme.typography.bodyMedium,
                color = PureWhite.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Card de Login
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
                        Text(
                            text = "Bienvenido",
                            style = MaterialTheme.typography.titleLarge,
                            color = Charcoal,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Inicia sesión para continuar",
                            style = MaterialTheme.typography.bodySmall,
                            color = Taupe
                        )
                        Spacer(modifier = Modifier.height(28.dp))

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
                                focusedBorderColor = Terracotta,
                                unfocusedBorderColor = Sand,
                                focusedLabelColor = Terracotta,
                                cursorColor = Terracotta
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))

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
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    if (email.isBlank() || password.isBlank()) {
                                        localError = "Completa todos los campos"
                                    } else {
                                        onLogin(email.trim(), password)
                                    }
                                }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Terracotta,
                                unfocusedBorderColor = Sand,
                                focusedLabelColor = Terracotta,
                                cursorColor = Terracotta
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Error
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

                        // Botón Login
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                if (email.isBlank() || password.isBlank()) {
                                    localError = "Completa todos los campos"
                                } else {
                                    localError = null
                                    onLogin(email.trim(), password)
                                }
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
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                LoadingSpinner(
                                    modifier = Modifier.size(24.dp),
                                    color = PureWhite,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Ingresar",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Enlace a Registro
                        TextButton(onClick = onNavigateToRegister) {
                            Text(
                                "¿No tienes cuenta? ",
                                color = Taupe,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Regístrate",
                                color = Terracotta,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
        }
    }
}
