package com.example.alertasismica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alertasismica.data.model.Earthquake
import com.example.alertasismica.data.repository.EarthquakeRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MagnitudeFilter(val label: String, val min: Double?) {
    ALL("Todos", null),
    M3("3.0+", 3.0),
    M45("4.5+", 4.5),
    M6("6.0+", 6.0)
}

data class QuakeListUiState(
    val quakes: List<Earthquake> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val filter: MagnitudeFilter = MagnitudeFilter.ALL,
    val onlyPeru: Boolean = true
)

class QuakeListViewModel(
    private val repository: EarthquakeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuakeListUiState())
    val state: StateFlow<QuakeListUiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun onFilterChange(filter: MagnitudeFilter) {
        _state.update { it.copy(filter = filter) }
        load()
    }

    fun onOnlyPeruChange(onlyPeru: Boolean) {
        _state.update { it.copy(onlyPeru = onlyPeru) }
        load()
    }

    fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val current = _state.value
            try {
                val quakes = repository.getEarthquakes(current.filter.min, current.onlyPeru)
                _state.update { it.copy(quakes = quakes, isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudieron cargar los sismos.\nRevisa tu conexión a internet."
                    )
                }
            }
        }
    }
}
