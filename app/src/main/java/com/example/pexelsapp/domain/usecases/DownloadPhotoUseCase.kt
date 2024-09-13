package com.example.pexelsapp.domain.usecases

import android.content.Context
import com.example.pexelsapp.domain.entities.Photo
import com.example.pexelsapp.domain.repository.PhotoRepository
import java.io.File

class DownloadPhotoUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(context: Context, photo: Photo, destination: File){
        repository.downloadPhoto(context, photo, destination)
    }
}