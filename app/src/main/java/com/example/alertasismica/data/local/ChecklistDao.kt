package com.example.alertasismica.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    @Query("SELECT * FROM checklist_items ORDER BY id ASC")
    fun observeAll(): Flow<List<ChecklistItemEntity>>

    @Insert
    suspend fun insert(item: ChecklistItemEntity): Long

    @Insert
    suspend fun insertAll(items: List<ChecklistItemEntity>)

    @Update
    suspend fun update(item: ChecklistItemEntity)

    @Query("UPDATE checklist_items SET checked = :checked WHERE id = :id")
    suspend fun setChecked(id: Int, checked: Boolean)

    @Query("DELETE FROM checklist_items WHERE id = :id")
    suspend fun deleteById(id: Int)
}
