package com.example.pexelsapp.domain.usecases

import com.example.pexelsapp.domain.entities.Photo
import com.example.pexelsapp.domain.repository.PhotoRepository

class AddPhotoToFavouritesUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(photo: Photo){
        repository.addPhotoToFavourites(photo)
    }
}