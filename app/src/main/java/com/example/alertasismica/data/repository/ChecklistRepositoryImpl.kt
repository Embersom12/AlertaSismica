package com.example.alertasismica.data.repository

import com.example.alertasismica.data.local.ChecklistDao
import com.example.alertasismica.data.local.ChecklistItemEntity
import kotlinx.coroutines.flow.Flow

class ChecklistRepositoryImpl(
    private val dao: ChecklistDao
) : ChecklistRepository {

    override fun observeAll(): Flow<List<ChecklistItemEntity>> = dao.observeAll()

    override suspend fun add(nombre: String, cantidad: Int) {
        dao.insert(ChecklistItemEntity(nombre = nombre, cantidad = cantidad))
    }

    override suspend fun update(item: ChecklistItemEntity) = dao.update(item)

    override suspend fun setChecked(id: Int, checked: Boolean) = dao.setChecked(id, checked)

    override suspend fun delete(id: Int) = dao.deleteById(id)

    override suspend fun addSuggestedItems() {
        dao.insertAll(
            listOf(
                ChecklistItemEntity(nombre = "Agua potable (litros)", cantidad = 6),
                ChecklistItemEntity(nombre = "Alimentos no perecibles", cantidad = 6),
                ChecklistItemEntity(nombre = "Linterna", cantidad = 1),
                ChecklistItemEntity(nombre = "Pilas de repuesto", cantidad = 4),
                ChecklistItemEntity(nombre = "Radio a pilas", cantidad = 1),
                ChecklistItemEntity(nombre = "Botiquín de primeros auxilios", cantidad = 1),
                ChecklistItemEntity(nombre = "Silbato", cantidad = 1),
                ChecklistItemEntity(nombre = "Copia de documentos", cantidad = 1),
                ChecklistItemEntity(nombre = "Mantas", cantidad = 2),
                ChecklistItemEntity(nombre = "Cargador portátil", cantidad = 1)
            )
        )
    }
}
