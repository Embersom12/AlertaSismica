package com.example.alertasismica.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["usuario"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val usuario: String,

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val email: String,

    val password: String
)
