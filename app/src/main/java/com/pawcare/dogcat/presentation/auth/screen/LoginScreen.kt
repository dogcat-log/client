package com.pawcare.dogcat.presentation.auth.screen

import Paddings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawcare.dogcat.presentation.auth.viewmodel.AuthViewModel
import com.pawcare.dogcat.R
import com.pawcare.dogcat.presentation.auth.state.UserState
import com.pawcare.dogcat.ui.theme.AppShapes
import com.pawcare.dogcat.ui.theme.AppTheme

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userState by viewModel.userState.collectAsState()
    val context = LocalContext.current

    // 구글 로그인 결과 처리
    val googleLoginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.processGoogleSignInResult(result.data)
    }

    LaunchedEffect(userState) {
        if (userState is UserState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // 앱 이름과 슬로건
        Text(
            text = "멍냥로그",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF444444)
        )

        Text(
            text = "소중한 우리 아이의 순간을 담아보세요.",
            color = Color(0xFF888888),
            style = MaterialTheme.typography.bodyLarge,
        )
        Image(
            painter = painterResource(id = R.drawable.boy_8233868_640),
            contentDescription = "App logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(vertical = Sizes.Image.small)
                .size(280.dp)
        )
        // 카카오 로그인
        Button(
//            onClick = { viewModel.handleKakaoLogin(context) },
            onClick = {
                onLoginSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.medium)
                .height(Sizes.Button.medium),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFE500),
                contentColor = Color.Black
            ),
            shape = AppShapes.medium
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_kakao),
                    contentDescription = "Kakao icon",
                    modifier = Modifier.size(Sizes.Icon.small)
                )
                Spacer(modifier = Modifier.width(Paddings.Space.small))
                Text(
                    text = "카카오 로그인",
                    style = MaterialTheme.typography.bodyMedium
                )
            }


        }

        // 구글 로그인 버튼
        Button(
            onClick = {
                val intent = viewModel.handleGoogleLogin(context)
                googleLoginLauncher.launch(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.medium)
                .height(Sizes.Button.medium),
            border = BorderStroke(0.5.dp, AppTheme.colors.primary),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            ),
            shape = AppShapes.medium
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google icon",
                    modifier = Modifier.size(Sizes.Image.tiny)
                )
                Spacer(modifier = Modifier.width(Paddings.Space.medium))
                Text(
                    text = "구글로 시작하기",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = { viewModel.handleNaverLogin(context) }

        ) {
            Row {
                Text(
                    text = "네이버 로그인",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }


        // 상태
        when (userState) {
            is UserState.Loading -> CircularProgressIndicator()
            is UserState.Error -> {
                Text(
                    text = (userState as UserState.Error).message,
                    color = AppTheme.colors.error
                )
            }

            else -> Unit
        }
        Spacer(modifier = Modifier.height(Paddings.Space.medium))
    }
}
