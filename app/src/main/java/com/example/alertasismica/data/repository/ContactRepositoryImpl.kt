package com.example.alertasismica.data.repository

import com.example.alertasismica.data.local.ContactDao
import com.example.alertasismica.data.local.ContactEntity
import kotlinx.coroutines.flow.Flow

class ContactRepositoryImpl(
    private val dao: ContactDao
) : ContactRepository {

    override fun search(query: String): Flow<List<ContactEntity>> = dao.search(query)

    override suspend fun getContact(id: Int): ContactEntity? = dao.getById(id)

    override suspend fun insert(contact: ContactEntity) {
        dao.insert(contact)
    }

    override suspend fun update(contact: ContactEntity) = dao.update(contact)

    override suspend fun delete(id: Int) = dao.deleteById(id)
}
