package com.example.pexelsapp.domain.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.pexelsapp.domain.entities.Photo
import java.io.File

interface PhotoRepository {
    suspend fun addPhotoToFavourites(photo: Photo)
    suspend fun downloadPhoto(context: Context, photo: Photo, destination: File)
    suspend fun fetchPhoto(id: Int): Photo
    suspend fun fetchFavouritePhotos(): LiveData<List<Photo>>
    suspend fun fetchPopularPhotos(perPage: Int): LiveData<List<Photo>>
}