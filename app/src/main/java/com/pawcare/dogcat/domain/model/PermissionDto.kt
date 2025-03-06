package com.pawcare.dogcat.domain.model

data class Permission(
    val type: PermissionType,
    val title: String,
    val description: String
)

enum class PermissionType {
    NOTIFICATION,
    CAMERA,
    LOCATION
}