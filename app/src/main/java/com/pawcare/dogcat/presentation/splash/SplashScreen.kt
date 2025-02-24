package com.pawcare.dogcat.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawcare.dogcat.R
import com.pawcare.dogcat.presentation.auth.state.UserState
import com.pawcare.dogcat.presentation.auth.viewmodel.AuthViewModel
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val userState by authViewModel.userState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Boy with dog"
        )
    }

    LaunchedEffect(key1 = true) {
        delay(1000) // 최소한의 스플래시 표시 시간
        val hasToken = authViewModel.hasValidToken()
        if (!hasToken) {
            onNavigateToLogin()
        }

    }

    // userState 변화 감지하여 네비게이션
    LaunchedEffect(userState) {
        when (userState) {
            is UserState.Success -> onNavigateToMain()
            is UserState.Error -> onNavigateToLogin()
            else -> {} // Loading, Initial 상태에서는 아무것도 하지 않음
        }
    }
}