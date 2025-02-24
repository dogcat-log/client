package com.pawcare.dogcat.domain.model.common

data class ApiResponse<T> (
    val status: String,
    val message: String,
    val data: T
)