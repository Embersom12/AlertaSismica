package com.example.alertasismica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alertasismica.data.local.ContactEntity
import com.example.alertasismica.data.repository.ContactRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val repository: ContactRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val contacts: StateFlow<List<ContactEntity>> = _query
        .flatMapLatest { repository.search(it.trim()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _toDelete = MutableStateFlow<ContactEntity?>(null)
    val toDelete: StateFlow<ContactEntity?> = _toDelete.asStateFlow()

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun askDelete(contact: ContactEntity) {
        _toDelete.value = contact
    }

    fun dismissDelete() {
        _toDelete.value = null
    }

    fun confirmDelete() {
        val contact = _toDelete.value ?: return
        _toDelete.value = null
        viewModelScope.launch { repository.delete(contact.id) }
    }
}
