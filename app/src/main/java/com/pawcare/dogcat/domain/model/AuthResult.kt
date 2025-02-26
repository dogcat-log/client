package com.pawcare.dogcat.domain.model

data class AuthResult(
    val accessToken: String,
    val refreshToken: String,
)