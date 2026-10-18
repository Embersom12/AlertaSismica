package com.example.alertasismica.data.repository

import com.example.alertasismica.data.model.Earthquake

interface EarthquakeRepository {

    suspend fun getEarthquakes(minMagnitude: Double?, onlyPeru: Boolean): List<Earthquake>

    fun findCached(id: String): Earthquake?
}
