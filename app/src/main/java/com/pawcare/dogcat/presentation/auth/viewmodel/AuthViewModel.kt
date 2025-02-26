package com.pawcare.dogcat.presentation.auth.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.pawcare.dogcat.R
import com.pawcare.dogcat.data.datastore.TokenDataStore
import com.pawcare.dogcat.domain.usecase.GetUserProfileUseCase
import com.pawcare.dogcat.domain.usecase.LoginSocialUseCase
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
    private val loginSocialUseCase: LoginSocialUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    companion object {
        private const val RC_SIGN_IN = 9001  // Google 로그인 요청 코드
    }

    private val _userState = MutableStateFlow<UserState>(UserState.Initial)
    val userState: StateFlow<UserState> = _userState.asStateFlow()


    init {
        checkTokenAndFetchProfile()
    }

    private fun checkTokenAndFetchProfile() {
        viewModelScope.launch {
            try {
                val token = tokenDataStore.accessToken.first()
                if (token != null) {
                    getUserProfile()
                } else {
                    _userState.value = UserState.Error("checkToken, 로그인이 필요합니다")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("초기화 중 오류가 발생했습니다")
            }
        }
    }


    sealed class SocialLoginProvider {
        object Kakao : SocialLoginProvider()
        object Google : SocialLoginProvider()
        object Naver : SocialLoginProvider()
    }


    // 공통 로그인 처리
    private fun processSocialLogin(provider: String, socialAccessToken: String) {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                loginSocialUseCase(provider, socialAccessToken)
                    .onSuccess { result ->
                        tokenDataStore.saveToken(result.accessToken, result.refreshToken)
                        getUserProfile()
                    }
                    .onFailure { e ->
                        tokenDataStore.clearToken() // 토큰 삭제
                        _userState.value = UserState.Error("로그인이 필요합니다 ${e.message}")
                    }
            } catch (e: Exception) {
                _userState.value = UserState.Error("로그인 처리중 오류 : ${e.message}")
            }
        }
    }


    /**
     * 제공자별 accessToken (카카오, 구글, 네이버)
     * **/

    fun handleKakaoLogin(context: Context) {
        UserApiClient.instance.run {
            if (isKakaoTalkLoginAvailable(context)) {
                loginWithKakaoTalk(context, callback = ::handleKakaoCallback)
            } else {
                loginWithKakaoAccount(context, callback = ::handleKakaoCallback)
            }
        }
    }

    private fun handleKakaoCallback(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            _userState.value = UserState.Error("카카오 로그인 실패 : `${error.message}")
            return
        }
        viewModelScope.launch {
            UserApiClient.instance.me { _, error ->
                token?.accessToken?.let { socialAccessToken ->
                    processSocialLogin("kakao", socialAccessToken)
                } ?: run {
                    _userState.value = UserState.Error("카카오 토큰을 받아오지 못했습니다. ${error?.message}")
                }
            }
        }
    }

    fun handleGoogleLogin(context: Context): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(context.getString(R.string.google_client_id))
            .requestIdToken(context.getString(1))
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient.signInIntent
    }

    fun processGoogleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { socialAccessToken ->
                processSocialLogin("google", socialAccessToken)
            } ?: run {
                _userState.value = UserState.Error("Google 토큰을 받아오지 못했습니다.")
            }
        } catch (e: Exception) {
            _userState.value = UserState.Error("Google 로그인 실패 `${e.message}.")

        }
    }

    fun handleNaverLogin(context: Context) {
        try {
            // 초기화
            NaverIdLoginSDK.initialize(
                context,
                context.getString(1),
                context.getString(1),
                context.getString(R.string.app_name)
//                context.getString(R.string.naver_client_id),
//                context.getString(R.string.naver_client_secret),
//                context.getString(R.string.app_name)
            )
            NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
                override fun onSuccess() {
                    val socialAccessToken = NaverIdLoginSDK.getAccessToken()
                    if (socialAccessToken != null) {
                        processSocialLogin("naver", socialAccessToken)
                    } else {
                        _userState.value = UserState.Error("네이버 토큰을 받아오지 못했습니다")
                    }
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    _userState.value = UserState.Error("네이버 로그인 실패 $message")
                }

                override fun onError(errorCode: Int, message: String) {
                    _userState.value = UserState.Error("네이버 로그인 에러 $message")
                }
            })
        } catch (e: Exception) {
            _userState.value = UserState.Error("네이버 초기화 실패 ${e.message}")

        }
    }


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

}