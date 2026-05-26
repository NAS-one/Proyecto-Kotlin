package com.osornofoodroutes.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = White,
    primaryContainer = OrangeLight,
    onPrimaryContainer = OrangeDark,
    secondary = GreenAccent,
    onSecondary = White,
    secondaryContainer = GreenLight,
    background = CreamBackground,
    onBackground = DarkText,
    surface = CardSurface,
    onSurface = DarkText,
    surfaceVariant = LightGray,
    onSurfaceVariant = SubtleText,
    error = ErrorRed,
    onError = White,
    outline = SubtleText
)

@Composable
fun OsornoFoodRoutesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
