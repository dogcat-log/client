package com.pawcare.dogcat.presentation.pet.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.pawcare.dogcat.domain.model.Pet

@HiltViewModel
class PetViewModel @Inject constructor() : ViewModel() {

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets = _pets.asStateFlow()

    init {
        // 임시 데이터
        _pets.value = listOf(
            Pet(
                id = "1",
                name = "멍멍이",
                type = "DOG",
                breed = "포메라니안",
                birthDate = "2020-01-01",
                imageUrl = "https://example.com/dog.jpg"
            ),
            Pet(
                id = "2",
                name = "냥이",
                type = "CAT",
                breed = "러시안블루",
                birthDate = "2021-02-02",
                imageUrl = "https://example.com/cat.jpg"
            ),
            Pet(
                id = "3",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),
            Pet(
                id = "4",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),
            Pet(
                id = "5",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),
            Pet(
                id = "6",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),Pet(
                id = "7",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),
            Pet(
                id = "8",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),Pet(
                id = "9",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),Pet(
                id = "10",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),Pet(
                id = "11",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            )
            ,Pet(
                id = "12",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),
            Pet(
                id = "13",
                name = "바둑이",
                type = "DOG",
                breed = "말티즈",
                birthDate = "2022-03-03",
                imageUrl = "https://example.com/dog2.jpg"
            ),




        )
    }
}