package com.pawcare.dogcat.presentation.main.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pawcare.dogcat.presentation.main.screen.MyPageScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MainNavItem.Home.route,
        modifier = modifier
    ) {
        composable(MainNavItem.Home.route) {
//            HomeScreen()
            Text("Home Screen")

        }
        composable(MainNavItem.MyPage.route) {
            MyPageScreen()
        }
        composable(MainNavItem.Settings.route) {
            Text("Settings Screen")
        }
    }
}