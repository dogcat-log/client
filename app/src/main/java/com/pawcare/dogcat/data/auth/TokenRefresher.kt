package com.pawcare.dogcat.data.auth

import com.pawcare.dogcat.core.ApiConstants
import com.pawcare.dogcat.data.api.RefreshTokenApi
import com.pawcare.dogcat.data.datastore.TokenDataStore
import com.pawcare.dogcat.domain.model.AuthResultDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenRefresher @Inject constructor(
    private val tokenDataStore: TokenDataStore
) {
    private val refreshClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val refreshApi: RefreshTokenApi by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.API_URL)
            .client(refreshClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RefreshTokenApi::class.java)
    }

    suspend fun refreshToken(refreshToken: String): Result<AuthResultDto> {
        return try {
            val response = refreshApi.refreshToken("Bearer $refreshToken")
            if (response.isSuccessful && response.body() != null) {
                val tokens = response.body()!!.data
                tokenDataStore.saveToken(tokens.accessToken, tokens.refreshToken)
                Result.success(tokens)
            } else {
                Result.failure(Exception("토큰 갱신 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}