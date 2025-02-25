package com.pawcare.dogcat.ui.theme.color

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.luminance


data class MyColors(
    val material: ColorScheme,
    val tertiary: Color = material.primary,
    val onPrimaryAlt: Color = material.onPrimary,
    val success: Color = Color.Green,
    val checked: Color = Color.White,
    val unchecked: Color = Color.White,
    val checkmark: Color = material.primary,
    val disabledSecondary: Color = material.secondary.copy(alpha = 0.5f),
    val backgroundVariant: Color = Color.LightGray,
    val textFiledBackgroundVariant: Color = Color.DarkGray,
    val launcherScreenBackground: Color = material.primary,
    val progressItemColor: Color = Color.Black,
    // 텍스트 색상 속성
    val textPrimary: Color = TextPrimary,
    val placeholder: Color = Placeholder,
    val primary :Color = primaryColorLight
) {
    val primaryContainer: Color get() = material.primaryContainer
    val secondary: Color get() = material.secondary
    val secondaryContainer: Color get() = material.secondaryContainer
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
    val isLight: Boolean get() = material.background.luminance() > 0.5f
}