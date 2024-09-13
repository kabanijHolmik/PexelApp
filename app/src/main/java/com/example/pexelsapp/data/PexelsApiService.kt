package com.example.pexelsapp.data


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.pexelsapp.data.RetrofitInstance.API_KEY
import com.example.pexelsapp.domain.entities.Photo

interface PexelsApiService {
    @Headers("Authorization: $API_KEY")
    @GET("search")
    suspend fun searchPhotos(@Query("query") query: String, @Query("per_page") perPage: Int): Call<List<Photo>>

    @Headers("Authorization: $API_KEY")
    @GET("curated")
    suspend fun getCuratedPhotos(@Query("per_page") perPage: Int): Call<List<Photo>>

    @Headers("Authorization: $API_KEY")
    @GET("photos/{id}")
    suspend fun getPhotoById(@Path("id") id: Int): Call<Photo>

}