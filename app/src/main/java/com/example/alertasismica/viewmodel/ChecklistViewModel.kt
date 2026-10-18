package com.example.alertasismica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alertasismica.data.local.ChecklistItemEntity
import com.example.alertasismica.data.repository.ChecklistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChecklistViewModel(
    private val repository: ChecklistRepository
) : ViewModel() {

    val items: StateFlow<List<ChecklistItemEntity>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _toDelete = MutableStateFlow<ChecklistItemEntity?>(null)
    val toDelete: StateFlow<ChecklistItemEntity?> = _toDelete.asStateFlow()

    fun add(nombre: String, cantidad: Int) {
        viewModelScope.launch { repository.add(nombre.trim(), cantidad) }
    }

    fun update(item: ChecklistItemEntity, nombre: String, cantidad: Int) {
        viewModelScope.launch {
            repository.update(item.copy(nombre = nombre.trim(), cantidad = cantidad))
        }
    }

    fun toggle(item: ChecklistItemEntity) {
        viewModelScope.launch { repository.setChecked(item.id, !item.checked) }
    }

    fun addSuggested() {
        viewModelScope.launch { repository.addSuggestedItems() }
    }

    fun askDelete(item: ChecklistItemEntity) {
        _toDelete.value = item
    }

    fun dismissDelete() {
        _toDelete.value = null
    }

    fun confirmDelete() {
        val item = _toDelete.value ?: return
        _toDelete.value = null
        viewModelScope.launch { repository.delete(item.id) }
    }
}
