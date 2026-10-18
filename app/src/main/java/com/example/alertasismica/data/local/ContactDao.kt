package com.example.alertasismica.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query(
        "SELECT * FROM contacts " +
            "WHERE nombre LIKE '%' || :query || '%' " +
            "OR telefono LIKE '%' || :query || '%' " +
            "OR relacion LIKE '%' || :query || '%' " +
            "ORDER BY nombre COLLATE NOCASE"
    )
    fun search(query: String): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ContactEntity?

    @Insert
    suspend fun insert(contact: ContactEntity): Long

    @Update
    suspend fun update(contact: ContactEntity)

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteById(id: Int)
}
