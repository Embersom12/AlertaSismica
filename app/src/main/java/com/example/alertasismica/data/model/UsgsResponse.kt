package com.example.alertasismica.data.model

import com.google.gson.annotations.SerializedName

data class UsgsResponse(
    @SerializedName("features") val features: List<UsgsFeature>?
)

data class UsgsFeature(
    @SerializedName("id") val id: String?,
    @SerializedName("properties") val properties: UsgsProperties?,
    @SerializedName("geometry") val geometry: UsgsGeometry?
)

data class UsgsProperties(
    @SerializedName("mag") val mag: Double?,
    @SerializedName("place") val place: String?,
    @SerializedName("time") val time: Long?,
    @SerializedName("url") val url: String?,
    @SerializedName("tsunami") val tsunami: Int?
)

data class UsgsGeometry(
    /** [longitud, latitud, profundidad en km] */
    @SerializedName("coordinates") val coordinates: List<Double>?
)
