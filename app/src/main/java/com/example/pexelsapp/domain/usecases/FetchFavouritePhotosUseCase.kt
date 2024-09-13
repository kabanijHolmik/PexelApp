package com.example.pexelsapp.domain.usecases

import com.example.pexelsapp.domain.repository.PhotoRepository

class FetchFavouritePhotosUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(){
        repository.fetchFavouritePhotos()
    }
}