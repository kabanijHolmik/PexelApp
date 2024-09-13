package com.example.pexelsapp.domain.usecases

import com.example.pexelsapp.domain.repository.PhotoRepository

class FetchPopularPhotosUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(perPage: Int){
        repository.fetchPopularPhotos(perPage)
    }
}