package com.pawcare.dogcat.ui.theme

import CustomTypography
import CustomTypographySet
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import com.pawcare.dogcat.ui.theme.color.ColorSet
import com.pawcare.dogcat.ui.theme.color.MyColors


val LocalCustomColors = staticCompositionLocalOf<MyColors> { error("No colors provided") }
val LocalCustomTypography =
    staticCompositionLocalOf<CustomTypography> { error("No typography provided") }

@Composable
fun DogCatLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        ColorSet.CustomColors.DarkColors
    } else {
        ColorSet.CustomColors.LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
        }
    }

    CompositionLocalProvider(
        LocalCustomColors provides colors,
        LocalCustomTypography provides CustomTypographySet,
        content = content
    )
}

// 테마 사용을 위한 헬퍼 객체
object AppTheme {
    val colors: MyColors
        @Composable
        get() = LocalCustomColors.current

    val typography: CustomTypography
        @Composable
        get() = LocalCustomTypography.current
}