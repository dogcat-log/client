package com.pawcare.dogcat.presentation.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.pawcare.dogcat.data.datastore.TokenDataStore
import com.pawcare.dogcat.domain.usecase.GetUserProfileUseCase
import com.pawcare.dogcat.domain.usecase.LoginWithKakaoUseCase
import com.pawcare.dogcat.presentation.auth.state.LoginState
import com.pawcare.dogcat.presentation.auth.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _userState = MutableStateFlow<UserState>(UserState.Initial)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    init {
        checkTokenAndFetchProfile()
    }

    private fun checkTokenAndFetchProfile() {
        viewModelScope.launch {
            tokenDataStore.token.collect { token ->
                if (token != null && userState.value !is UserState.Success) {
                    getUserProfile()
                }
            }
        }
    }

    fun handleKakaoLogin(context: Context) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            UserApiClient.instance.run {
                if (isKakaoTalkLoginAvailable(context)) {
                    loginWithKakaoTalk(context, callback = ::handleKakaoCallback)
                } else {
                    loginWithKakaoAccount(context, callback = ::handleKakaoCallback)
                }
            }
        }
    }

    private fun handleKakaoCallback(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            _loginState.value = LoginState.Error("카카오 로그인 실패: ${error.message}")
            return
        }
        viewModelScope.launch {
            UserApiClient.instance.me { user, error ->
                val email = user?.kakaoAccount?.email

                if (error != null || email == null || token == null) {
                    _loginState.value = LoginState.Error("사용자 정보 조회 실패")
                    return@me
                }
                viewModelScope.launch {
                    try {
                        loginWithKakaoUseCase(email, token.accessToken)
                            .onSuccess { result ->
                                tokenDataStore.saveToken(result.accessToken)
                                _loginState.value = LoginState.Success(result)
                            }
                            .onFailure { e ->
                                _loginState.value = LoginState.Error("서버 로그인 실패: ${e.message}")
                            }
                    } catch (e: Exception) {
                        _loginState.value = LoginState.Error("로그인 처리 중 오류 발생: ${e.message}")
                    }
                }
            }
        }
    }

    // 기존 메서드들 유지
    fun getUserProfile() {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                getUserProfileUseCase()
                    .onSuccess { response ->
                        _userState.value = UserState.Success(response.data)
                    }
                    .onFailure { exception ->
                        _userState.value =
                            UserState.Error(exception.message ?: "사용자 정보를 가져오는데 실패했습니다.")
                    }
            } catch (e: Exception) {
                _userState.value = UserState.Error("예상치 못한 오류가 발생했습니다.")
            }
        }
    }

    suspend fun hasValidToken(): Boolean {
        return tokenDataStore.token.first() != null
    }
}