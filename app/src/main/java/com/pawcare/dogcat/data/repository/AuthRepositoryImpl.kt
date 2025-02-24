package com.pawcare.dogcat.data.repository

import android.content.Context
import android.util.Log
import com.pawcare.dogcat.data.api.AuthApi
import com.pawcare.dogcat.data.datastore.TokenDataStore
import com.pawcare.dogcat.domain.model.AuthResult
import com.pawcare.dogcat.domain.model.User
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

    override suspend fun loginWithKakao(email: String, accessToken: String): Result<AuthResult> {
        return try {
            val response = authApi.loginWithKakao(
                accessToken = accessToken,
                provider = "KAKAO"
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("카카오 로그인 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): Result<ApiResponse<User>> {
        return try {
            val token =
                tokenDataStore.token.first() ?: return Result.failure(Exception("토큰을 찾을 수 없습니다."))
            Log.d("getUserProfileToken", token)

            val response = authApi.getUserProfile("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("사용자 정보를 가져오는데 실패했습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}