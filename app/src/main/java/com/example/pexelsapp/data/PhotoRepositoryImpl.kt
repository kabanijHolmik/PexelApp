package com.example.pexelsapp.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.pexelsapp.domain.entities.Photo
import com.example.pexelsapp.domain.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class PhotoRepositoryImpl(application: Application) : PhotoRepository {
    private val apiService = RetrofitInstance.getInstance().create(PexelsApiService::class.java)
    private val photoDao = AppDatabase.getInstance(application).photoDao()
    override suspend fun addPhotoToFavourites(photo: Photo) {
        photoDao.addFavouritePhoto(PhotoDBModel(photo.id))
    }

    override suspend fun downloadPhoto(context: Context, photo: Photo, destination: File) {

        try {
            val futureTarget = Glide.with(context)
                .asFile()
                .load(photo.src.original)
                .submit()

            val file = withContext(Dispatchers.IO) {
                futureTarget.get()
            }
            file.copyTo(destination, overwrite = true)

            Glide.with(context).clear(futureTarget)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun fetchPhoto(id: Int): Photo = withContext(Dispatchers.IO) {
        val response = apiService.getPhotoById(id).execute()
        if (response.isSuccessful) response.body() ?: throw Exception("Photo not found")
        else throw Exception("Error fetching photo")
    }

    override suspend fun fetchFavouritePhotos(): LiveData<List<Photo>> {
        val favouritePhotosId = photoDao.getFavouritesPhotosId()
        val mutableLiveDataPhotos = MutableLiveData<List<Photo>>()
        val photosList = mutableListOf<Photo>()

        favouritePhotosId.forEach {
            val response = apiService.getPhotoById(it.photoId).execute()
            if (response.isSuccessful) {
                response.body()?.let { photo ->
                    photosList.add(photo)
                }
            }
        }
        mutableLiveDataPhotos.postValue(photosList)
        return mutableLiveDataPhotos
    }

    override suspend fun fetchPopularPhotos(perPage: Int): LiveData<List<Photo>> {
        val favouritePhotos = MutableLiveData<List<Photo>>()

        withContext(Dispatchers.IO) {
            val response = apiService.getCuratedPhotos(perPage).execute()
            if (response.isSuccessful) {
                favouritePhotos.postValue(response.body())
            } else {
                throw Exception("Error fetching photos")
            }
        }

        return favouritePhotos
    }
}