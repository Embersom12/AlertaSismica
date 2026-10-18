package com.example.alertasismica.data.repository

import com.example.alertasismica.data.local.SavedQuakeEntity
import com.example.alertasismica.data.model.Earthquake
import kotlinx.coroutines.flow.Flow

interface SavedQuakeRepository {
    fun search(query: String): Flow<List<SavedQuakeEntity>>
    fun observeIsSaved(quakeId: String): Flow<Boolean>
    suspend fun findByQuakeId(quakeId: String): SavedQuakeEntity?
    suspend fun save(quake: Earthquake)
    suspend fun updateNote(id: Int, note: String)
    suspend fun delete(id: Int)
    suspend fun deleteByQuakeId(quakeId: String)
}
