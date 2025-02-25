package com.pawcare.dogcat.presentation.auth.screen

import Paddings
import android.util.Log
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
import com.pawcare.dogcat.presentation.auth.state.LoginState
import com.pawcare.dogcat.presentation.auth.viewmodel.AuthViewModel
import com.pawcare.dogcat.R
import com.pawcare.dogcat.ui.theme.AppShapes

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginState by viewModel.loginState.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
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
//                .padding(vertical = Sizes.Image.small)
//                .size(280.dp)
        )
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
//        Button(
//            onClick = { viewModel.handleSocialLogin("GOOGLE") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(Paddings.medium)
//                .height(Sizes.Button.medium),
//            border = BorderStroke(0.5.dp, LocalColors.current.placeholder),
//            colors = ButtonDefaults.outlinedButtonColors(
//                contentColor = Color.Black
//            ),
//            shape = AppShapes.medium
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_google),
//                    contentDescription = "Google icon",
//                    modifier = Modifier.size(Sizes.Image.tiny)
//                )
//                Spacer(modifier = Modifier.width(Paddings.Space.medium))
//                Text(
//                    text = "구글로 시작하기",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }


        Log.d("loginState", loginState.toString())
        // 로그인 상태 표시
        when (loginState) {
            is LoginState.Loading -> CircularProgressIndicator()
            is LoginState.Error -> {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = Color.Red
                )
            }

            else -> Unit
        }
        Spacer(modifier = Modifier.height(Paddings.Space.medium))
    }
}
