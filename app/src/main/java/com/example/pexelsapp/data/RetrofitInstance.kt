package com.example.pexelsapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL ="https://api.pexels.com/v1/"
    const val API_KEY = "w3EeSWraDMgcEC1QompPiAVfy4izNqGNXQlWN67R4mVcB05goQu4jdoW"
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create())
                .build()
    }
}