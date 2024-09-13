package com.example.pexelsapp.domain.usecases

import com.example.pexelsapp.domain.repository.PhotoRepository

class FetchPhotoUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(id: Int){
        repository.fetchPhoto(id)
    }
}