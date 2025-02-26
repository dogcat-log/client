package com.pawcare.dogcat.presentation.permission.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.pawcare.dogcat.domain.model.Permission
import com.pawcare.dogcat.domain.model.PermissionType
import com.pawcare.dogcat.presentation.permission.state.PermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _permissionState = MutableStateFlow<PermissionState>(PermissionState.Initial)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()


    // 시스템 권한 매핑
    private val systemPermission = mapOf(
        // 안드로이드 13 이상 실행, 12까지는 알림 권한이 자동 허용
        PermissionType.NOTIFICATION to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else null,
        PermissionType.CAMERA to Manifest.permission.CAMERA,
        PermissionType.LOCATION to Manifest.permission.ACCESS_FINE_LOCATION
    )

    init {
        initializePermissions()
    }

    private fun initializePermissions() {
        val permission = listOf(
            Permission(
                type = PermissionType.NOTIFICATION,
                title = "알림",
                description = "푸시 알림 및 메시지 수신 안내"
            ),
            Permission(
                type = PermissionType.CAMERA,
                title = "사진/카메라",
                description = "펫 내 사진 업로드"
            ),
            Permission(
                type = PermissionType.LOCATION,
                title = "산책을 위한 ",
                description = "산책 트래킹"
            )
        )
        _permissionState.value = PermissionState.Success(permission)
    }

    fun getRequiredSystemPermissions(): List<String> {
        val currentPermissions = (_permissionState.value as? PermissionState.Success)?.permissions ?: return emptyList()
        return currentPermissions
            .mapNotNull { systemPermission[it.type] }
            .filter { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }
    }

    fun areAllPermissionsGranted(): Boolean {
        return getRequiredSystemPermissions().all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

}