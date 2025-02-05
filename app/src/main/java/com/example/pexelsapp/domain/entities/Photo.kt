package com.example.pexelsapp.domain.entities

data class Photo(
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographerUrl: String,
    val photographerId: Int,
    val avgColor: String,
    val src: Src,
    val liked: Boolean,
    val alt: String
)