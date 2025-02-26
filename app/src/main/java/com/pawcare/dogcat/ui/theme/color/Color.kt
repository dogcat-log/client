package com.pawcare.dogcat.ui.theme.color

import androidx.compose.ui.graphics.Color

/**
 * 커스텀 컬러는 여기에 지정합니다.
 * */


data class ColorsStyles(
    val basicBlack: Color = Color(0xFF000000),
    val basicWhite: Color = Color(0xFFFFFFFF),
    val basicGrayLight : Color = Color(0xFFD8D8D8),
    val basicGrayLightMedium2 : Color = Color(0xFFE6E5E9),
    val basicGrayDark : Color = Color(0xFF323232),
    val basicLightMedium : Color = Color(0xB0B0B0FF),
    val warmGreen : Color = Color(0xFF167A66),
    val warmGreen2 : Color = Color(0xFFCFE6E2),
    val warmBeige : Color = Color(0xFFF1E4C0),
)


//data class ButtonColors(
//    val default = Color()
//)

// 기본 색상 정의
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Grey100 = Color(0xFFF5F5F5)
val Grey200 = Color(0xFFEEEEEE)
val Grey900 = Color(0xFF212121)

// 커스텀 색상 정의
val CustomPrimary = Color(0xFF025414)
val CustomSecondary = Color(0xFF388E3C)
val CustomError = Color(0xFFF44336)
val CustomBackground = Color(0xFFFAFAFA)
val CustomSurface = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF000000)
val Placeholder = Color(0xFF999999)


sealed class ColorSet {
    open lateinit var LightColors: MyColors
    open lateinit var DarkColors: MyColors

    object CustomColors : ColorSet() {
        // 라이트 모드 색상
        override var LightColors = MyColors(
            primary = CustomPrimary,
            secondary = CustomSecondary,
            background = CustomBackground,
            surface = CustomSurface,
            error = CustomError,
            onPrimary = White,
            onSecondary = White,
            onBackground = Black,
            onSurface = Black,
            textPrimary = TextPrimary,
            placeholder = Placeholder,
            success = CustomSecondary,
            disabledSecondary = Grey200,
            backgroundVariant = Grey100,
            colorsStyles = ColorsStyles(),
        )
        // 다크 모드 색상
        override var DarkColors = MyColors(
            primary = CustomPrimary.copy(alpha = 0.8f),
            secondary = CustomSecondary.copy(alpha = 0.8f),
            background = Black,
            surface = Grey900,
            error = CustomError.copy(alpha = 0.8f),
            onPrimary = White,
            onSecondary = White,
            onBackground = White,
            onSurface = White,
            textPrimary = White,
            placeholder = Grey200,
            success = CustomSecondary.copy(alpha = 0.8f),
            disabledSecondary = Grey900,
            backgroundVariant = Grey900,
            colorsStyles = ColorsStyles(),

            )
    }
}
