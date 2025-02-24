package com.pawcare.dogcat.presentation.auth.state

import com.pawcare.dogcat.domain.model.User


sealed class UserState {
    data object Initial : UserState()
    data object Loading : UserState()
    data class Success(val user : User) : UserState()
    data class Error(val message : String) : UserState()
}