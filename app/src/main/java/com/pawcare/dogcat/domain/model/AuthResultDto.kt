package com.pawcare.dogcat.domain.model

data class AuthResultDto(
    val accessToken: String,
    val refreshToken: String,
)