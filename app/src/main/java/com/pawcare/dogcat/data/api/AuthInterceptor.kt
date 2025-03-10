package com.pawcare.dogcat.data.api

import android.util.Log
import com.pawcare.dogcat.data.auth.TokenRefresher
import com.pawcare.dogcat.data.datastore.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val tokenRefresher: TokenRefresher
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // auth 관련 요청은 토큰 없이 통과
        if (originalRequest.url.encodedPath.contains("/api/auth/refresh") ||
            originalRequest.url.encodedPath.contains("/api/auth/login/oauth2")
        ) {
            return chain.proceed(originalRequest)
        }

        // 나머지 요청에는 토큰 추가
        val accessToken = runBlocking { tokenDataStore.accessToken.first() }
        val response = chain.proceed(
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        )

        // 401/403 에러 처리
        if (response.code == 401 || response.code == 403) {
            response.close()

            val refreshToken = runBlocking { tokenDataStore.refreshToken.first() }
            if (refreshToken != null) {
                try {
                    Log.d("Auth", "토큰 만료, 갱신 시도")
                    val refreshResult = runBlocking {
                        tokenRefresher.refreshToken(refreshToken)
                    }

                    if (refreshResult.isSuccess) {
                        Log.d("Auth", "토큰 갱신 성공, 요청 재시도")
                        // 원래 요청 재시도
                        return chain.proceed(
                            originalRequest.newBuilder()
                                .header("Authorization", "Bearer ${refreshResult.getOrNull()?.accessToken}")
                                .build()
                        )
                    } else {
                        Log.e("Auth", "토큰 갱신 실패: ${refreshResult.exceptionOrNull()?.message}")
                    }
                } catch (e: Exception) {
                    Log.e("Auth", "토큰 갱신 중 오류 발생", e)
                }
            }
        }

        return response
    }
}