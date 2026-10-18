package com.example.alertasismica.data.repository

import com.example.alertasismica.data.model.WeatherInfo
import com.example.alertasismica.data.remote.OpenMeteoApiService

class WeatherRepositoryImpl(
    private val api: OpenMeteoApiService
) : WeatherRepository {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherInfo {
        val response = api.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            current = "temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code",
            timezone = "auto"
        )
        val current = response.current
            ?: throw IllegalStateException("La API no devolvió el clima actual")

        return WeatherInfo(
            temperature = current.temperature ?: 0.0,
            humidity = current.humidity ?: 0.0,
            windSpeed = current.windSpeed ?: 0.0,
            weatherCode = current.weatherCode ?: 0
        )
    }
}
