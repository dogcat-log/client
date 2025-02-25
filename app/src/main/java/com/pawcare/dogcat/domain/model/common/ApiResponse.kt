package com.pawcare.dogcat.domain.model.common

data class ApiResponse<T> (
    val success: String,
    val message: String,
    val data: T
)