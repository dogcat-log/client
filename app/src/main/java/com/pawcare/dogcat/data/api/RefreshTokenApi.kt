package com.pawcare.dogcat.data.api

import com.pawcare.dogcat.domain.model.AuthResultDto
import com.pawcare.dogcat.domain.model.common.ApiResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Response



interface RefreshTokenApi {
    @POST("/api/auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String
    ): Response<ApiResponse<AuthResultDto>>
}
