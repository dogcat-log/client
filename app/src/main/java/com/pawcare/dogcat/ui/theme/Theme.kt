package com.pawcare.dogcat.ui.theme

import AppTypography
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.pawcare.dogcat.ui.theme.color.ColorSet


val LocalColors = staticCompositionLocalOf {
    ColorSet.CustomColors.LightColors
}

private val LocalTypography = staticCompositionLocalOf {
    AppTypography
}

@Composable
fun DogCatLogTheme(
    myColors: ColorSet = ColorSet.CustomColors,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> myColors.DarkColors.material
        else -> myColors.LightColors.material
    }

    val colors = if (darkTheme) myColors.DarkColors else myColors.LightColors
    val typography = LocalTypography.current

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = AppShapes,
            content = content
        )
    }
}