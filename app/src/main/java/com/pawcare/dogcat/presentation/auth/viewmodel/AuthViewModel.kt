package com.pawcare.dogcat.presentation.auth.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.pawcare.dogcat.domain.repository.AuthRepository
import com.pawcare.dogcat.domain.usecase.GetUserProfileUseCase
import com.pawcare.dogcat.domain.usecase.LoginSocialUseCase
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
    private val tokenDataStore: TokenDataStore,
    private val authRepository: AuthRepository
) : ViewModel() {

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
                    getUserProfileUseCase()
                        .onSuccess { response ->
                            _userState.value = UserState.Success(response.data)
                        }
                        .onFailure { exception ->
                            _userState.value = UserState.Error("사용자 정보를 가져오는데 실패했습니다: ${exception.message}")
                        }
                } else {
                    _userState.value = UserState.Error("checkToken, 로그인이 필요합니다")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("초기화 중 오류가 발생했습니다")
            }
        }
    }

    fun handleKakaoLogin(context: Context) {
        Log.d("KakaoLogin", "카카오 로그인 시작")

        // 카카오톡 설치 여부 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡 로그인
            Log.d("KakaoLogin", "카카오톡 앱으로 로그인 시도")
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                handleKakaoCallback(token, error)
            }
        } else {
            // 카카오 계정 로그인
            Log.d("KakaoLogin", "카카오 계정으로 로그인 시도")
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                handleKakaoCallback(token, error)
            }
        }
    }

    private fun handleKakaoCallback(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Log.e("KakaoLogin", "카카오 로그인 실패: ${error.message}", error)
            _userState.value = UserState.Error("카카오 로그인 실패: ${error.message}")
            return
        }

        if (token == null) {
            Log.e("KakaoLogin", "카카오 토큰이 null입니다")
            _userState.value = UserState.Error("카카오 토큰을 받아오지 못했습니다")
            return
        }

        Log.d("KakaoLogin", "카카오 로그인 성공: 토큰=${token.accessToken.take(10)}...")

        // 사용자 정보 요청
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("KakaoLogin", "사용자 정보 요청 실패: ${error.message}", error)
                _userState.value = UserState.Error("카카오 사용자 정보 요청 실패: ${error.message}")
                return@me
            }

            if (user == null) {
                Log.e("KakaoLogin", "카카오 사용자 정보가 null입니다")
                _userState.value = UserState.Error("카카오 사용자 정보를 받아오지 못했습니다")
                return@me
            }

            Log.d("KakaoLogin", "사용자 정보 요청 성공: ID=${user.id}, 이메일=${user.kakaoAccount?.email}")

            // 서버에 소셜 로그인 요청
            processSocialLogin("kakao", token.accessToken)
        }
    }

    // 공통 로그인 처리
    private fun processSocialLogin(provider: String, socialAccessToken: String) {
        Log.d("NaverLogin-common", "$provider 로그인 처리 시작: 토큰=${socialAccessToken.take(10)}...")

        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                Log.d("NaverLogin-common", "$provider API 호출 시작")

                loginSocialUseCase(provider, socialAccessToken)
                    .onSuccess { result ->
                        Log.d("NaverLogin-common", "$provider 로그인 성공: JWT 토큰 발급됨")
                        tokenDataStore.saveToken(result.data.accessToken, result.data.refreshToken)
                        Log.d("NaverLogin", tokenDataStore.accessToken.first().toString())
                        Log.d("NaverLogin-common", "토큰 저장 완료, 프로필 정보 요청 시작")

                        getUserProfileUseCase()
                            .onSuccess { response ->
                                _userState.value = UserState.Success(response.data)
                            }
                            .onFailure { exception ->
                                _userState.value =
                                    UserState.Error("사용자 정보를 가져오는데 실패했습니다: ${exception.message}")
                            }
                    }
                    .onFailure { e ->
                        Log.e("NaverLogin-common", "$provider 로그인 실패: ${e.message}", e)
                        tokenDataStore.clearToken()
                        _userState.value = UserState.Error("로그인이 필요합니다 ${e.message}")
                    }
            } catch (e: Exception) {
                Log.e("NaverLogin-common", "$provider 로그인 처리 중 예외 발생: ${e.message}", e)
                _userState.value = UserState.Error("로그인 처리중 오류 : ${e.message}")
            }
        }
    }


    /**
     * 제공자별 accessToken (카카오, 구글, 네이버)
     * **/
    fun handleGoogleLogin(context: Context): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_client_id))
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
            _userState.value = UserState.Error("Google 로그인 실패 `${e.message} + $e")
        }
    }

    fun handleNaverLogin(context: Context) {
        try {
            // 초기화 로그 추가
            Log.d("NaverLogin", "SDK 초기화 시작: ID=${context.getString(R.string.naver_client_id)}")

            // 초기화
            NaverIdLoginSDK.initialize(
                context,
                context.getString(R.string.naver_client_id),
                context.getString(R.string.naver_client_secret),
                context.getString(R.string.app_name)
            )

            Log.d("NaverLogin", "SDK 초기화 완료, 인증 시작")

            NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
                override fun onSuccess() {
                    val socialAccessToken = NaverIdLoginSDK.getAccessToken()
                    Log.d("NaverLogin", "인증 성공: 토큰=${socialAccessToken?.take(10)}...")

                    if (socialAccessToken != null) {
                        // val result = 제거 (반환값이 없는 함수)
                        processSocialLogin("naver", socialAccessToken)
                        Log.d("NaverLogin", "processSocialLogin 호출됨")
                    } else {
                        Log.e("NaverLogin", "토큰이 null임")
                        _userState.value = UserState.Error("네이버 토큰을 받아오지 못했습니다")
                    }
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.e("NaverLogin", "인증 실패: 상태=$httpStatus, 메시지=$message")
                    _userState.value = UserState.Error("네이버 로그인 실패 $message")
                }

                override fun onError(errorCode: Int, message: String) {
                    Log.e("NaverLogin", "인증 오류: 코드=$errorCode, 메시지=$message")
                    _userState.value = UserState.Error("네이버 로그인 에러 $message")
                }
            })
        } catch (e: Exception) {
            Log.e("NaverLogin", "초기화 실패: ${e.message}", e)
            _userState.value = UserState.Error("네이버 초기화 실패 ${e.message}")
        }
    }


}