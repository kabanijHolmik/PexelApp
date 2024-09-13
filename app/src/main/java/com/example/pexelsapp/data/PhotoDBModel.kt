package com.example.pexelsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class PhotoDBModel(
    @PrimaryKey
    val photoId: Int)
