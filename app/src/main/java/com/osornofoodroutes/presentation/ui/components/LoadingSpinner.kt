package com.osornofoodroutes.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spinner de carga personalizado que reemplaza CircularProgressIndicator de Material3.
 * Usa Canvas + rememberInfiniteTransition para evitar el bug de KeyframesSpec.at()
 * que causa NoSuchMethodError en versiones incompatibles de Compose.
 */
@Composable
fun LoadingSpinner(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidth: Dp = 3.dp,
    size: Dp = 24.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spinner")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    Canvas(
        modifier = modifier.size(size)
    ) {
        drawArc(
            color = color,
            startAngle = rotation,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}
