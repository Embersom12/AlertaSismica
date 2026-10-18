package com.example.alertasismica.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedQuakeDao {

    @Query(
        "SELECT * FROM saved_quakes " +
            "WHERE place LIKE '%' || :query || '%' OR note LIKE '%' || :query || '%' " +
            "ORDER BY saved_at DESC"
    )
    fun search(query: String): Flow<List<SavedQuakeEntity>>

    @Query("SELECT COUNT(*) FROM saved_quakes WHERE quake_id = :quakeId")
    fun observeCount(quakeId: String): Flow<Int>

    @Query("SELECT * FROM saved_quakes WHERE quake_id = :quakeId LIMIT 1")
    suspend fun findByQuakeId(quakeId: String): SavedQuakeEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: SavedQuakeEntity): Long

    @Query("UPDATE saved_quakes SET note = :note WHERE id = :id")
    suspend fun updateNote(id: Int, note: String)

    @Query("DELETE FROM saved_quakes WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM saved_quakes WHERE quake_id = :quakeId")
    suspend fun deleteByQuakeId(quakeId: String)
}
