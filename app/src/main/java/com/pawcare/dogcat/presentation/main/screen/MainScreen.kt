package com.pawcare.dogcat.presentation.main.screen

import Paddings
import Sizes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.pawcare.dogcat.domain.model.Pet
import com.pawcare.dogcat.presentation.pet.viewmodel.PetViewModel
import com.pawcare.dogcat.ui.theme.AppTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: PetViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showSettingsModel by remember { mutableStateOf(false) }
    val pets by viewModel.pets.collectAsState()

    var selectedPet by remember { mutableStateOf<Pet?>(null) }
    // 설정 모달
    if (showSettingsModel) {
        BasicAlertDialog(
            onDismissRequest = { showSettingsModel = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = selectedPet?.name ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                TextButton(
                    onClick = { showSettingsModel = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        "수정",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                TextButton(
                    onClick = { showSettingsModel = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        "삭제",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Paddings.huge)
                        .background(AppTheme.colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "내 반려동물",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(pets) { pet ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                //반려동물 선택처리
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 이미지와 이름을 묶는 Row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    AsyncImage(
                                        model = pet.imageUrl,
                                        contentDescription = "반려동물 이미지",
                                        modifier = Modifier
                                            .size(Sizes.Image.small)
                                            .background(
                                                AppTheme.colors.primary,
                                                CircleShape
                                            )
                                    )
                                    Text(
                                        text = pet.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                // 설정 아이콘
                                IconButton(
                                    onClick = {
                                        selectedPet = pet
                                        showSettingsModel = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "설정"
                                    )
                                }
                            }
                        }
                    }
                }

                NavigationDrawerItem(
                    label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "반려동물 설정",
                                modifier = Modifier.padding(end = Paddings.small)
                            )
                            Text(
                                "추가하기",
                                textAlign = TextAlign.Center
                            )

                        }
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .height(40.dp)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "메뉴 열기"
                            )
                        }
                    },
                    windowInsets = WindowInsets(0, 0, 0, 0)
                )
            },
            // 3-2. 하단 네비게이션 바
            bottomBar = { MainBottomNavigation(navController = navController) }
        ) { paddingValues ->
            // 3-3. 메인 콘텐츠 영역
            Box(modifier = Modifier.padding(paddingValues)) {
                MainNavHost(navController = navController)
            }
        }
    }
}


