package com.example.alertasismica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alertasismica.data.local.SavedQuakeEntity
import com.example.alertasismica.data.repository.SavedQuakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedQuakesViewModel(
    private val repository: SavedQuakeRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val quakes: StateFlow<List<SavedQuakeEntity>> = _query
        .flatMapLatest { repository.search(it.trim()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _toDelete = MutableStateFlow<SavedQuakeEntity?>(null)
    val toDelete: StateFlow<SavedQuakeEntity?> = _toDelete.asStateFlow()

    private val _toEditNote = MutableStateFlow<SavedQuakeEntity?>(null)
    val toEditNote: StateFlow<SavedQuakeEntity?> = _toEditNote.asStateFlow()

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun askDelete(item: SavedQuakeEntity) {
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

    fun askEditNote(item: SavedQuakeEntity) {
        _toEditNote.value = item
    }

    fun dismissEditNote() {
        _toEditNote.value = null
    }

    fun saveNote(note: String) {
        val item = _toEditNote.value ?: return
        _toEditNote.value = null
        viewModelScope.launch { repository.updateNote(item.id, note.trim()) }
    }
}
