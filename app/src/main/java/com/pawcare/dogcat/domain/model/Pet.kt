package com.pawcare.dogcat.domain.model


data class Pet(
    val id: String,
    val name: String,
    val type: String, // DOG or CAT
    val breed: String,
    val birthDate: String,
    val imageUrl: String?
)