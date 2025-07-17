package com.example.sabcvasampleapp.neha.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode

// SABC color scheme
private val SABCPrimary = Color(0xFFB00020)
private val SABCSecondary = Color(0xFFF2F2F2)
private val SABCTertiary = Color(0xFFF9F9F9)
private val SABCOnPrimary = Color.White
private val SABCOnSecondary = Color.Black

// 🎨 Gradient options for events with no image
val DefaultEventGradients = listOf(
    Brush.verticalGradient(
        colors = listOf(Color( 0xFFB43FFC), Color(0xFFD3C6FF))
    ),
    Brush.verticalGradient(
        colors = listOf(Color(0xFF268FD0), Color(0xFFCEF2EA)) // blue to navy
    ),
    Brush.verticalGradient(
        colors = listOf(Color(0xFFDADFFF), Color(0xFF4286f4)) // slate to blue
    ),
    Brush.verticalGradient(
        colors = listOf(Color(0xFFee9ca7), Color(0xFFffdde1)) // pink pastel
    ),
    Brush.verticalGradient(
        colors = listOf(Color(0xFF11998e), Color(0xFF38ef7d)) // teal to green
    ),
    Brush.verticalGradient(
        colors = listOf(Color(0xFFfc4a1a), Color(0xFFf7b733)) // orange to yellow
    )
)

private val DarkColorScheme = darkColorScheme(
    primary = SABCPrimary,
    secondary = SABCSecondary,
    tertiary = SABCTertiary,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = SABCOnPrimary,
    onSecondary = SABCOnSecondary,
    onTertiary = SABCOnSecondary,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = SABCPrimary,
    secondary = SABCSecondary,
    tertiary = SABCTertiary,
    background = Color.White,
    surface = Color.White,
    onPrimary = SABCOnPrimary,
    onSecondary = SABCOnSecondary,
    onTertiary = SABCOnSecondary,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun SABCVASampleAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
