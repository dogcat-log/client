package com.pawcare.dogcat.ui.theme.color

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme

// 라이트 모드, 다크 모드에서 사용할 색상 정의
val primaryColorLight = Color(0xFF025414)  // 라이트 모드에서 사용할 녹색
val primaryColorDark = Color(0xFF000000)  // 다크 모드에서 사용할 블랙

// 기본 색상들
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Grey100 = Color(0xFFF5F5F5)
val Grey200 = Color(0xFFEEEEEE)
val Red400 = Color(0xFFF44336)
val Red700 = Color(0xFFD32F2F)
val Green400 = Color(0xFF388E3C)
val TextPrimary = Color(0xFF000000)
val Placeholder = Color(0xFF999999)

sealed class ColorSet {
    open lateinit var LightColors: MyColors
    open lateinit var DarkColors: MyColors

    object CustomColors : ColorSet() {
        // 라이트 모드 색상 설정
        override var LightColors = MyColors(
            // Material3 기본 색상 스키마
            material = lightColorScheme(
                primary = primaryColorLight,        // 앱의 주요 브랜드 색상 (녹색)
                primaryContainer = primaryColorLight.copy(alpha = 0.8f),  // 주요 색상의 밝은 변형 (20% 투명도)
                secondary = Green400,               // 보조 색상 (녹색)
                secondaryContainer = Grey200,       // 보조 색상의 밝은 변형 (연한 회색)
                surface = White,                    // 카드나 시트 같은 표면 요소의 색상
                onSurface = Black,                  // surface 위에 올라가는 텍스트/아이콘 색상
                background = White,                 // 화면 배경 색상
                onBackground = Black,               // 배경 위에 올라가는 텍스트/아이콘 색상
                error = Red400,                    // 에러 상태를 표시하는 색상
                onPrimary = White,                  // primary 색상 위에 올라가는 텍스트/아이콘 색상

            ),

            // 추가 커스텀 색상들
            success = Green400,                    // 성공 상태를 표시하는 색상
            disabledSecondary = Grey200,          // 비활성화된 보조 요소의 색상
            backgroundVariant = Grey100,           // 배경색의 변형 (매우 연한 회색)
            // 텍스트 색상
            textPrimary = TextPrimary,
            placeholder = Placeholder
        )

        // 다크 모드 색상 설정
        override var DarkColors = MyColors(
            material = darkColorScheme(
                primary = primaryColorDark,
                primaryContainer = primaryColorDark.copy(alpha = 0.8f),
                secondary = Green400,
                secondaryContainer = Grey200,
                surface = Black,
                onSurface = White,
                background = Black,
                onBackground = White,
                error = Red400,
                onPrimary = White
            ),
            //텍스트 색상
            textPrimary = TextPrimary,
            placeholder = Placeholder

        )
    }
}