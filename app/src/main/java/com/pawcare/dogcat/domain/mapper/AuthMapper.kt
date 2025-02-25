package com.pawcare.dogcat.domain.mapper

import com.pawcare.dogcat.domain.model.AuthResult
import com.pawcare.dogcat.domain.model.User
import com.pawcare.dogcat.domain.model.common.ApiResponse

object AuthMapper {
    fun toAuthResult(response : AuthResult): AuthResult{
        return AuthResult(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }

    fun toUser(apiResponse: ApiResponse<User>): User {
        return apiResponse.data
    }
    
}