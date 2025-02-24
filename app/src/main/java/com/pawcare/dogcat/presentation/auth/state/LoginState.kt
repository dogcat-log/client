package com.pawcare.dogcat.presentation.auth.state

import com.pawcare.dogcat.domain.model.AuthResult

sealed class LoginState {
    data object Initial : LoginState()
    data object Loading : LoginState()
    data class Success(val result: AuthResult) : LoginState()
    data class Error(val message: String) : LoginState()
}
