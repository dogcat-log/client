package com.pawcare.dogcat.domain.model.common

data class ApiResponse<T> (
    val success: Boolean,
    val message: String? = null,
    val data: T
)