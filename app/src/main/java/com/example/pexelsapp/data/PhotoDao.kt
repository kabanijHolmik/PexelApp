package com.example.pexelsapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoDao {
    @Query("SELECT * FROM PHOTO")
    suspend fun getFavouritesPhotosId(): List<PhotoDBModel>

    @Query("DELETE FROM PHOTO WHERE photoId = :photoId")
    suspend fun deleteFavouritePhoto(photoId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavouritePhoto(photo: PhotoDBModel)

}