package com.pawcare.dogcat.domain.repository

import com.pawcare.dogcat.domain.model.AuthResult
import com.pawcare.dogcat.domain.model.User
import com.pawcare.dogcat.domain.model.common.ApiResponse

interface AuthRepository {
    suspend fun socialLogin(email: String, accessToken: String): Result<AuthResult>
    suspend fun getUserProfile(): Result<ApiResponse<User>>
}
