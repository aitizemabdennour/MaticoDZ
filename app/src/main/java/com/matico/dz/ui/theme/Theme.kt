package com.matico.dz.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MaticoPrimary,
    onPrimary = Color.White,
    primaryContainer = MaticoPrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF111827),
    surface = Color(0xFF1F2937),
    error = MaticoError
)

private val LightColorScheme = lightColorScheme(
    primary = MaticoPrimary,
    onPrimary = Color.White,
    primaryContainer = MaticoPrimaryLight,
    onPrimaryContainer = MaticoPrimaryDark,
    secondary = MaticoTextSecondary,
    tertiary = MaticoWarning,
    background = MaticoBackground,
    surface = MaticoSurface,
    onBackground = MaticoTextPrimary,
    onSurface = MaticoTextPrimary,
    error = MaticoError
)

@Composable
fun MaticoDZTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled for brand consistency
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
