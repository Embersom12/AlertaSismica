package com.example.alertasismica.data.remote

import com.example.alertasismica.data.model.UsgsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UsgsApiService {

    @GET("fdsnws/event/1/query")
    suspend fun getEarthquakes(
        @Query("format") format: String,
        @Query("orderby") orderBy: String,
        @Query("limit") limit: Int,
        @Query("minmagnitude") minMagnitude: Double?,
        @Query("minlatitude") minLatitude: Double?,
        @Query("maxlatitude") maxLatitude: Double?,
        @Query("minlongitude") minLongitude: Double?,
        @Query("maxlongitude") maxLongitude: Double?
    ): UsgsResponse
}
