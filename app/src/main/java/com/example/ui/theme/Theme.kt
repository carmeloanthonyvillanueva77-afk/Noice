package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ForestDarkColorScheme = darkColorScheme(
    primary = ForestSoftSage,
    onPrimary = ForestDeepEmerald,
    primaryContainer = ForestMedium,
    onPrimaryContainer = ForestPastelSage,
    secondary = ForestMint,
    onSecondary = CrispWhite,
    secondaryContainer = ForestDarkCard,
    onSecondaryContainer = ForestPastelSage,
    background = ForestDarkBackground,
    onBackground = CrispWhite,
    surface = ForestDarkSurface,
    onSurface = CrispWhite,
    surfaceVariant = ForestDarkCard,
    onSurfaceVariant = ForestSoftSage,
    outline = ForestDarkCardBorder
)

private val ForestLightColorScheme = lightColorScheme(
    primary = ForestDeepEmerald,
    onPrimary = CrispWhite,
    primaryContainer = ForestPastelSage,
    onPrimaryContainer = ForestDeepEmerald,
    secondary = ForestMedium,
    onSecondary = CrispWhite,
    secondaryContainer = ForestUltraLight,
    onSecondaryContainer = ForestDeepEmerald,
    background = Color(0xFFF7FBF9),
    onBackground = SlateDark,
    surface = CrispWhite,
    onSurface = SlateDark,
    surfaceVariant = ForestUltraLight,
    onSurfaceVariant = SlateMedium,
    outline = SlateBorder
)

@Composable
fun HabitFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ForestDarkColorScheme else ForestLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    HabitFlowTheme(darkTheme = darkTheme, content = content)
}
