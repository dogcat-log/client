package com.pawcare.dogcat.domain.mapper

import com.pawcare.dogcat.data.model.User
import com.pawcare.dogcat.domain.model.UserDto

fun User.toDomain(): UserDto {
    return UserDto(
//        uid = this.uid
        userEmail = this.userEmail,
//        userName = this.userName,
//        phoneNumber = this.phoneNumber,
//        authProvider = this.authProvider,
//        profileImageUrl = this.profileImageUrl,
//        userRoles = this.userRoles,
//        familyActive = this.familyActive,
//        familyName = this.familyName,
    )
}
