package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TrekDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF00363D),
    onPrimaryContainer = NeonCyan,
    secondary = NeonPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A0A1F),
    onSecondaryContainer = Color(0xFFFFD6E0),
    tertiary = WhatsAppGreen,
    onTertiary = Color.Black,
    background = DarkNavyBackground,
    onBackground = TextPrimary,
    surface = DarkSurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = DarkSurfaceBorder,
    outlineVariant = Color(0xFF152238)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded aesthetic on all devices
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = TrekDarkColorScheme,
        typography = Typography,
        content = content
    )
}
