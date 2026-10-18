package com.example.alertasismica.data.repository

import com.example.alertasismica.data.local.SavedQuakeDao
import com.example.alertasismica.data.local.SavedQuakeEntity
import com.example.alertasismica.data.local.toSavedEntity
import com.example.alertasismica.data.model.Earthquake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedQuakeRepositoryImpl(
    private val dao: SavedQuakeDao
) : SavedQuakeRepository {

    override fun search(query: String): Flow<List<SavedQuakeEntity>> = dao.search(query)

    override fun observeIsSaved(quakeId: String): Flow<Boolean> =
        dao.observeCount(quakeId).map { it > 0 }

    override suspend fun findByQuakeId(quakeId: String): SavedQuakeEntity? =
        dao.findByQuakeId(quakeId)

    override suspend fun save(quake: Earthquake) {
        dao.insert(quake.toSavedEntity())
    }

    override suspend fun updateNote(id: Int, note: String) = dao.updateNote(id, note)

    override suspend fun delete(id: Int) = dao.deleteById(id)

    override suspend fun deleteByQuakeId(quakeId: String) = dao.deleteByQuakeId(quakeId)
}
