package com.pawcare.dogcat.data.repository

import android.content.Context
import android.util.Log
import com.pawcare.dogcat.data.api.AuthApi
import com.pawcare.dogcat.data.datastore.TokenDataStore
import com.pawcare.dogcat.domain.model.AuthResultDto
import com.pawcare.dogcat.data.model.User
import com.pawcare.dogcat.domain.model.common.ApiResponse
import com.pawcare.dogcat.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenDataStore: TokenDataStore,
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun socialLogin(
        provider: String,
        accessToken: String
    ): Result<ApiResponse<AuthResultDto>> {
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
            Log.d("NaverLogin-profile", "1. 프로필 조회 시작")

            val token = tokenDataStore.accessToken.first()
            Log.d("NaverLogin-profile", "2. 토큰 조회 완료: ${token?.take(10)}...")

            if (token == null) {
                Log.e("NaverLogin-profile", "토큰이 없음")
                return Result.failure(Exception("토큰이 없습니다"))
            }

            Log.d("NaverLogin-profile", "3. API 호출 직전")
            val fullToken = "Bearer $token"
            Log.d("NaverLogin-profile", "Authorization: ${fullToken.take(20)}...")

            val response = authApi.getUserProfile(fullToken)
            Log.d("NaverLogin-profile", "4. API 응답 수신")

            if (response.isSuccessful) {
                Log.d("NaverLogin-profile", "5. 응답 성공: ${response.body()}")
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("응답 데이터 없음"))
            } else {
                Log.e(
                    "NaverLogin-profile",
                    "5. 응답 실패: ${response.code()} - ${response.errorBody()?.string()}"
                )
                Result.failure(Exception("프로필 조회 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("NaverLogin-profile", "프로필 조회 중 에러", e)
            Result.failure(e)
        }
    }

}