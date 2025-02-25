package com.pawcare.dogcat.domain.usecase

import com.pawcare.dogcat.domain.model.AuthResult
import com.pawcare.dogcat.domain.repository.AuthRepository
import javax.inject.Inject


class LoginWithKakaoUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, accessToken: String) : Result<AuthResult>{
        return authRepository.loginWithKakao(email, accessToken)
    }
}