package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = MintPrimaryDark,
    onPrimary = Color(0xFF003822),
    primaryContainer = MintDarkContainer,
    onPrimaryContainer = MintPrimaryDark,
    secondary = CyanAccentDark,
    onSecondary = Color(0xFF003549),
    secondaryContainer = Color(0xFF004D6B),
    onSecondaryContainer = Color(0xFFC7E7FF),
    tertiary = CoralEnergyDark,
    onTertiary = Color(0xFF512400),
    tertiaryContainer = Color(0xFF733600),
    onTertiaryContainer = Color(0xFFFFDCC5),
    background = DarkBg,
    onBackground = Color(0xFFF1F5F9),
    surface = DarkSurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = MintPrimary,
    onPrimary = Color.White,
    primaryContainer = MintPrimaryContainer,
    onPrimaryContainer = Color(0xFF002113),
    secondary = CyanAccent,
    onSecondary = Color.White,
    secondaryContainer = CyanAccentContainer,
    onSecondaryContainer = Color(0xFF001F2B),
    tertiary = CoralEnergy,
    onTertiary = Color.White,
    tertiaryContainer = CoralContainer,
    onTertiaryContainer = Color(0xFF341100),
    background = LightBg,
    onBackground = Color(0xFF0F172A),
    surface = LightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF475569),
    outline = LightOutline
)

@Composable
fun FitFlowTheme(
    darkTheme: Boolean = true, // FitFlow sports aesthetic shines with immersive dark theme by default
    dynamicColor: Boolean = false, // Keep signature FitFlow energetic teal/cyan branding
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

// Alias for template compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) = FitFlowTheme(darkTheme, dynamicColor, content)
