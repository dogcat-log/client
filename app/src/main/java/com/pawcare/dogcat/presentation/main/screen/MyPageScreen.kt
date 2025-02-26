package com.pawcare.dogcat.presentation.main.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawcare.dogcat.presentation.main.components.UserProfileContent
import com.pawcare.dogcat.presentation.auth.viewmodel.AuthViewModel

@Composable
fun MyPageScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val userState by viewModel.userState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Paddings.large)
    ) {
        Text(
            text = "마이페이지",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = Paddings.large)
        )

        UserProfileContent(
            userState = userState,
            modifier = Modifier.fillMaxSize()
        )
    }
}