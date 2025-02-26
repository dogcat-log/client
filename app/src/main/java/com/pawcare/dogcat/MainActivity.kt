package com.pawcare.dogcat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import android.content.pm.PackageManager
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pawcare.dogcat.presentation.auth.screen.LoginScreen
import com.pawcare.dogcat.presentation.auth.state.UserState
import com.pawcare.dogcat.presentation.auth.viewmodel.AuthViewModel
import com.pawcare.dogcat.presentation.main.screen.MainScreen
import com.pawcare.dogcat.presentation.splash.SplashScreen
import com.pawcare.dogcat.ui.theme.DogCatLogTheme
import java.security.MessageDigest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        // 키 해시 로그 출력 코드 추가
//        try {
//            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures!!) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT)
//                Log.d("KeyHash", "키 해시: $keyHash")
//            }
//        } catch (e: Exception) {
//            Log.e("KeyHash", "키 해시 얻기 실패", e)
//        }

        enableEdgeToEdge()

        setContent {
            DogCatLogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        authViewModel = authViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val userState by authViewModel.userState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen()

            LaunchedEffect(userState) {
                when (userState) {
                    is UserState.Success -> {
                        navController.navigate("main") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }

                    is UserState.Error -> {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }

                    else -> {}
                }
            }
        }

        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen()
        }
    }
}