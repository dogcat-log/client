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
import com.pawcare.dogcat.presentation.main.components.PetDrawerContent
import com.pawcare.dogcat.presentation.main.components.PetSettingDialog
import com.pawcare.dogcat.presentation.main.navigation.MainBottomNavigation
import com.pawcare.dogcat.presentation.main.navigation.MainNavHost
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            PetDrawerContent(
                pets = pets,
                onPetSettingsClick = { pet ->
                    selectedPet = pet
                    showSettingsModel = true
                },
                onAddPetClick = { scope.launch { drawerState.close() } }
            )
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
            // 네비바
            bottomBar = { MainBottomNavigation(navController = navController) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MainNavHost(navController = navController)
            }
        }
    }

    PetSettingDialog(
        selectedPet = selectedPet,
        showDialog = showSettingsModel,
        onDismiss = { showSettingsModel = false }
    )


}


