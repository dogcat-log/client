package com.pawcare.dogcat.data.repository

import android.content.Context
import android.util.Log
import com.pawcare.dogcat.data.api.AuthApi
import com.pawcare.dogcat.data.datastore.TokenDataStore
import com.pawcare.dogcat.domain.model.AuthResultDto
import com.pawcare.dogcat.data.model.User
import com.pawcare.dogcat.domain.mapper.toAuthResult
import com.pawcare.dogcat.domain.model.common.ApiResponse
import com.pawcare.dogcat.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.log

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenDataStore: TokenDataStore,
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun socialLogin(provider: String, accessToken: String): Result<ApiResponse<AuthResultDto>> {
        try {
            val response = authApi.socialLogin(accessToken, provider)

            if (response.isSuccessful) {
                return response.body()?.let { apiResponse ->
                    Result.success(apiResponse)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                return Result.failure(Exception("소셜 로그인 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): Result<ApiResponse<User>> {
        return try {
            val token =
                tokenDataStore.accessToken.first() ?: return Result.failure(Exception("토큰이 없습니다"))


            val response = authApi.getUserProfile("Bearer $token")
            Log.d("NaverLogin-common", "$response")


            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("응답 데이터 없음"))
            } else {
                Result.failure(Exception("프로필 조회 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}