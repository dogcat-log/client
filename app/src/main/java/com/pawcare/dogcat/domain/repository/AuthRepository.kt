package com.pawcare.dogcat.domain.repository

import com.pawcare.dogcat.domain.model.AuthResultDto
import com.pawcare.dogcat.data.model.User
import com.pawcare.dogcat.domain.model.common.ApiResponse

interface AuthRepository {
    suspend fun socialLogin(provider: String, accessToken: String): Result<ApiResponse<AuthResultDto>>
    suspend fun getUserProfile(): Result<ApiResponse<User>>
}
