package com.pawcare.dogcat.domain.usecase

import com.pawcare.dogcat.domain.model.AuthResult
import com.pawcare.dogcat.domain.repository.AuthRepository
import javax.inject.Inject


class LoginSocialUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: String, accessToken: String) : Result<AuthResult>{
        return authRepository.socialLogin(provider, accessToken)
    }
}