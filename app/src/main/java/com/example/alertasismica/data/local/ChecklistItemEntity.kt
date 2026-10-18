package com.example.alertasismica.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val cantidad: Int = 1,
    val checked: Boolean = false
)
