package com.pawcare.dogcat.presentation.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pawcare.dogcat.presentation.auth.state.UserState

@Composable
fun UserProfileContent(
    userState: UserState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Paddings.large)
    ) {
        when (userState) {
            is UserState.Loading -> {
                CircularProgressIndicator()
            }

            is UserState.Success -> {
                val user = userState.user
                Column {
                    Text(
                        text = "프로필 정보",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(Paddings.large)
                    )

//                    ProfileItem(
//                        label = "이름",
//                        value = user.userName ?: "이름 없음"
//                    )
                    ProfileItem(
                        label = "이메일",
                        value = user.userEmail ?: "이메일 없음"
                    )
//                    ProfileItem(
//                        label = "전화번호",
//                        value = user.phoneNumber ?: "전화번호 미등록"
//                    )
//                    ProfileItem(
//                        label = "프로필 이미지",
//                        value = user.profileImageUrl ?: "프로필 이미지 없음"
//                    )
//
//                    if (user.familyActive) {
//                        user.familyName?.let { familyName ->
//                            ProfileItem(
//                                label = "패밀리 이름",
//                                value = familyName
//                            )
//                        }
//                    }
                }
            }

            is UserState.Error -> {
                Text(
                    text = userState.message,
                    color = Color.Red,
                    modifier = Modifier.padding(Paddings.large)
                )
            }

            UserState.Initial -> {
                Text(
                    text = "사용자 정보를 불러오는 중...",
                    modifier = Modifier.padding(Paddings.large)
                )
            }
        }
    }
}

@Composable
private fun ProfileItem(
    label: String,
    value: String?, // nullable로 변경
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Paddings.small)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value ?: "", // null 처리
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(Paddings.large))
    }
}