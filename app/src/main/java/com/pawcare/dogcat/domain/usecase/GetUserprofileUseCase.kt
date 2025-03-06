package com.pawcare.dogcat.domain.usecase

import com.pawcare.dogcat.data.model.User
import com.pawcare.dogcat.domain.model.common.ApiResponse
import com.pawcare.dogcat.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<ApiResponse<User>> {
        return authRepository.getUserProfile()
    }
}