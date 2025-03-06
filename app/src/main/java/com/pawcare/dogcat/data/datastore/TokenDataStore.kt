package com.pawcare.dogcat.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    // 토큰 저장
    suspend fun saveToken(jwtAccessToken: String, jwtRefreshToken: String) {
        try {
            Log.d("TokenDebug", "saveToken 시작")
            context.dataStore.edit { preferences ->
                Log.d("TokenDebug", "preferences 편집 중")
                preferences[ACCESS_TOKEN_KEY] = jwtAccessToken
                preferences[REFRESH_TOKEN_KEY] = jwtRefreshToken
                Log.d("TokenDebug", "preferences 편집 완료")
            }
            Log.d("TokenDebug", "dataStore.edit 완료")

            // 저장 직후 바로 읽기 시도
            val savedPreferences = context.dataStore.data.first()
            val savedAccessToken = savedPreferences[ACCESS_TOKEN_KEY]
            Log.d("TokenDebug", "저장 직후 읽은 토큰: $savedAccessToken")
        } catch (e: Exception) {
            Log.e("TokenDebug", "토큰 저장/읽기 중 오류: ${e.message}", e)
        }
    }

    // 액세스 토큰 가져오기
    val accessToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]
    }

    // 리프레시 토큰 가져오기
    val refreshToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }

    // 토큰 삭제(로그아웃 시 사용)
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
}
