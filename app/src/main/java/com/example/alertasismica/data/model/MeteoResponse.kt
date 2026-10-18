package com.example.alertasismica.data.model

import com.google.gson.annotations.SerializedName


data class MeteoResponse(
    @SerializedName("current") val current: MeteoCurrent?
)

data class MeteoCurrent(
    @SerializedName("temperature_2m") val temperature: Double?,
    @SerializedName("relative_humidity_2m") val humidity: Double?,
    @SerializedName("wind_speed_10m") val windSpeed: Double?,
    @SerializedName("weather_code") val weatherCode: Int?
)
