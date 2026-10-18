package com.example.alertasismica.data.repository

import com.example.alertasismica.data.local.ContactEntity
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun search(query: String): Flow<List<ContactEntity>>
    suspend fun getContact(id: Int): ContactEntity?
    suspend fun insert(contact: ContactEntity)
    suspend fun update(contact: ContactEntity)
    suspend fun delete(id: Int)
}
