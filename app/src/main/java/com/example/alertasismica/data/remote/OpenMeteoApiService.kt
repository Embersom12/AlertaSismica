package com.example.alertasismica.data.remote

import com.example.alertasismica.data.model.MeteoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApiService {

    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query(value = "current", encoded = true) current: String,
        @Query("timezone") timezone: String
    ): MeteoResponse
}
