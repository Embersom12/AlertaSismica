package com.example.alertasismica.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.alertasismica.data.model.Earthquake

@Entity(
    tableName = "saved_quakes",
    indices = [Index(value = ["quake_id"], unique = true)]
)
data class SavedQuakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "quake_id")
    val quakeId: String,

    val magnitude: Double,
    val place: String,

    @ColumnInfo(name = "time_millis")
    val timeMillis: Long,

    val latitude: Double,
    val longitude: Double,

    @ColumnInfo(name = "depth_km")
    val depthKm: Double,

    val url: String,
    val tsunami: Boolean,

    val note: String = "",

    @ColumnInfo(name = "saved_at")
    val savedAt: Long = System.currentTimeMillis()
)

fun SavedQuakeEntity.toEarthquake() = Earthquake(
    id = quakeId,
    magnitude = magnitude,
    place = place,
    timeMillis = timeMillis,
    latitude = latitude,
    longitude = longitude,
    depthKm = depthKm,
    url = url,
    tsunami = tsunami
)

fun Earthquake.toSavedEntity() = SavedQuakeEntity(
    quakeId = id,
    magnitude = magnitude,
    place = place,
    timeMillis = timeMillis,
    latitude = latitude,
    longitude = longitude,
    depthKm = depthKm,
    url = url,
    tsunami = tsunami
)
