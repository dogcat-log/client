package com.pawcare.dogcat.presentation.permission.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawcare.dogcat.presentation.permission.components.PermissionItem
import com.pawcare.dogcat.presentation.permission.state.PermissionState
import com.pawcare.dogcat.presentation.permission.viewmodel.PermissionViewModel

@Composable
fun PermissionScreen(
    modifier: Modifier = Modifier,
    viewModel: PermissionViewModel = hiltViewModel(),
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val permissionState by viewModel.permissionState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val allGranted = permissionsMap.values.all { it }
        if (allGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "필수 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "원활한 서비스 이용을 위해\n다음 권한의 허용이 필요합니다.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )

            when (permissionState) {
                is PermissionState.Success -> {
                    val permissions = (permissionState as PermissionState.Success).permissions
                    permissions.forEach { permission ->
                        PermissionItem(
                            permission = permission,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                is PermissionState.Error -> {
                    Text(
                        text = (permissionState as PermissionState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                }
                else -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "* 선택적 접근권한의 경우에도 동의하지 않으면 해당 서비스는 이용하실 수 없습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    val permissions = viewModel.getRequiredSystemPermissions()
                    if (permissions.isNotEmpty()) {
                        permissionLauncher.launch(permissions.toTypedArray())
                    } else {
                        onPermissionGranted()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("확인")
            }
        }
    }
}