package com.pawcare.dogcat.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pawcare.dogcat.domain.model.Pet
import com.pawcare.dogcat.ui.theme.AppTheme

@Composable
fun PetDrawerContent(
    pets: List<Pet>,
    onPetSettingsClick: (Pet) -> Unit,
    onAddPetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 상단 헤더
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(AppTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "내 반려동물",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // 반려동물 목록과 "추가하기" 버튼
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // 남은 공간을 채움
            ) {
                // 반려동물 목록
                items(pets) { pet ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { /* 반려동물 선택 처리 추가 가능 */ }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AsyncImage(
                                    model = pet.imageUrl,
                                    contentDescription = "반려동물 이미지",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(AppTheme.colors.primary, CircleShape)
                                )
                                Text(
                                    text = pet.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            IconButton(onClick = { onPetSettingsClick(pet) }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "설정")
                            }
                        }
                    }
                }

                // "추가하기" 버튼을 리스트 마지막에 추가
                item {
                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "반려동물 설정",
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = "추가하기",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        },
                        selected = false,
                        onClick = onAddPetClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp) // 리스트 하단 여백
                    )
                }
            }
        }
    }
}