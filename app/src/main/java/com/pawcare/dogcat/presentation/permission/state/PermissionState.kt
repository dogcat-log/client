package com.pawcare.dogcat.presentation.permission.state

import com.pawcare.dogcat.domain.model.Permission

sealed class PermissionState {
    object Initial : PermissionState()
    object Loading : PermissionState()
    data class Success(val permissions: List<Permission>) : PermissionState()
    data class Error(val message: String) : PermissionState()
}