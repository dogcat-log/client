package com.pawcare.dogcat.data.api

import com.pawcare.dogcat.domain.model.AuthResultDto
import com.pawcare.dogcat.data.model.User
import com.pawcare.dogcat.domain.model.common.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @GET("/api/user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<User>>

    @POST("/api/auth/login/oauth2")
    suspend fun socialLogin(
        @Query("accessToken") accessToken: String,
        @Query("provider") provider: String
    ): Response<ApiResponse<AuthResultDto>>
}