package com.example.alertasismica.data.remote

import com.example.alertasismica.constant.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val usgsApi: UsgsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.USGS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsgsApiService::class.java)
    }

    val meteoApi: OpenMeteoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.METEO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoApiService::class.java)
    }
}
