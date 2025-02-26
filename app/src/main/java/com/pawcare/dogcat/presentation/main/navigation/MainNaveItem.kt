package com.pawcare.dogcat.presentation.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : MainNavItem(
        route = "home",
        title = "홈",
        icon = Icons.Default.Home
    )

    data object MyPage : MainNavItem(
        route = "mypage",
        title = "마이페이지",
        icon = Icons.Default.Person
    )

    data object Settings : MainNavItem(
        route = "settings",
        title = "설정",
        icon = Icons.Default.Settings
    )

    companion object {
        val items = listOf(Home, MyPage, Settings)
    }
}