package com.pawcare.dogcat.data.repository

import android.content.Context
import com.pawcare.dogcat.data.api.AuthApi
import com.pawcare.dogcat.data.datastore.TokenDataStore
import com.pawcare.dogcat.domain.mapper.AuthMapper
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

    override suspend fun socialLogin(provider: String, accessToken: String): Result<AuthResult> {
        return try {
            val response = authApi.socialLogin(accessToken, provider)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(AuthMapper.toAuthResult(it))
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
                tokenDataStore.accessToken.first() ?: return Result.failure(Exception("토큰이 없습니다"))

            val response = authApi.getUserProfile("Bearer $token")
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