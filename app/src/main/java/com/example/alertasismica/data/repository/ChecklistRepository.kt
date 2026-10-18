package com.example.alertasismica.data.repository

import com.example.alertasismica.data.local.ChecklistItemEntity
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun observeAll(): Flow<List<ChecklistItemEntity>>
    suspend fun add(nombre: String, cantidad: Int)
    suspend fun update(item: ChecklistItemEntity)
    suspend fun setChecked(id: Int, checked: Boolean)
    suspend fun delete(id: Int)
    suspend fun addSuggestedItems()
}
