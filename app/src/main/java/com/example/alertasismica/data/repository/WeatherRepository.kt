package com.example.alertasismica.data.repository

import com.example.alertasismica.data.model.WeatherInfo

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherInfo
}
