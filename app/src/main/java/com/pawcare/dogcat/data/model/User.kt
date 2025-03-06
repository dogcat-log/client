package com.pawcare.dogcat.data.model

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("uid")
    val uid: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("authProvider")
    val authProvider: String,
    @SerializedName("profileImageUrl")
    val profileImageUrl: String?,
    @SerializedName("userRoles")
    val userRoles: String,
    @SerializedName("familyActive")
    val familyActive: Boolean,
    @SerializedName("familyName")
    val familyName: String?
)