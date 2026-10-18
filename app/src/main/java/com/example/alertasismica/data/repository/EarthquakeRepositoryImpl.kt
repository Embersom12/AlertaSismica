package com.example.alertasismica.data.repository

import com.example.alertasismica.constant.Constants
import com.example.alertasismica.data.model.Earthquake
import com.example.alertasismica.data.remote.UsgsApiService
import java.util.concurrent.ConcurrentHashMap

class EarthquakeRepositoryImpl(
    private val api: UsgsApiService
) : EarthquakeRepository {

    private val cache = ConcurrentHashMap<String, Earthquake>()

    override suspend fun getEarthquakes(minMagnitude: Double?, onlyPeru: Boolean): List<Earthquake> {
        val response = api.getEarthquakes(
            format = "geojson",
            orderBy = "time",
            limit = 100,
            minMagnitude = minMagnitude,
            minLatitude = if (onlyPeru) Constants.PERU_MIN_LAT else null,
            maxLatitude = if (onlyPeru) Constants.PERU_MAX_LAT else null,
            minLongitude = if (onlyPeru) Constants.PERU_MIN_LON else null,
            maxLongitude = if (onlyPeru) Constants.PERU_MAX_LON else null
        )

        val quakes = response.features.orEmpty().mapNotNull { feature ->
            val props = feature.properties ?: return@mapNotNull null
            val coords = feature.geometry?.coordinates
            val id = feature.id
            val mag = props.mag
            if (id == null || mag == null || coords == null || coords.size < 2) {
                return@mapNotNull null
            }
            Earthquake(
                id = id,
                magnitude = mag,
                place = props.place ?: "Ubicación desconocida",
                timeMillis = props.time ?: 0L,
                longitude = coords[0],
                latitude = coords[1],
                depthKm = coords.getOrNull(2) ?: 0.0,
                url = props.url.orEmpty(),
                tsunami = (props.tsunami ?: 0) == 1
            )
        }

        quakes.forEach { cache[it.id] = it }
        return quakes
    }

    override fun findCached(id: String): Earthquake? = cache[id]
}
