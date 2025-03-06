package com.pawcare.dogcat.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pawcare.dogcat.MainActivity
import com.pawcare.dogcat.presentation.auth.screen.LoginScreen
import com.pawcare.dogcat.presentation.auth.state.UserState
import com.pawcare.dogcat.presentation.auth.viewmodel.AuthViewModel
import com.pawcare.dogcat.presentation.permission.screen.PermissionScreen
import com.pawcare.dogcat.presentation.permission.viewmodel.PermissionViewModel
import com.pawcare.dogcat.presentation.splash.SplashScreen
import com.pawcare.dogcat.ui.theme.DogCatLogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogCatLogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthNavigation(
                        authViewModel = authViewModel,
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToMain = { navigateToMainActivity() })
                }

            }

        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 인증 Activity 종료
    }
}


@Composable
fun AuthNavigation(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    onNavigateToMain: () -> Unit
) {
    val navController = rememberNavController()
    val userState by authViewModel.userState.collectAsState()
    val permissionViewModel: PermissionViewModel = hiltViewModel()
    val allPermissionsGranted by rememberUpdatedState(permissionViewModel.areAllPermissionsGranted())

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen()
            LaunchedEffect(userState, allPermissionsGranted) {
                when (userState) {
                    is UserState.Success -> {
                        if (allPermissionsGranted) {
                            onNavigateToMain()
                        } else {
                            navController.navigate("permission") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }

                    is UserState.Error -> navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }

                    else -> {}
                }
            }
        }

        composable("permission") {
            PermissionScreen(
                onPermissionGranted = {
                    onNavigateToMain()
                }
            )
        }
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    if (allPermissionsGranted) {
                        onNavigateToMain()
                    } else {
                        navController.navigate("permission") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}