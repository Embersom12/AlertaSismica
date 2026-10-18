package com.example.alertasismica.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alertasismica.data.local.toEarthquake
import com.example.alertasismica.data.model.Earthquake
import com.example.alertasismica.data.model.WeatherInfo
import com.example.alertasismica.data.repository.EarthquakeRepository
import com.example.alertasismica.data.repository.SavedQuakeRepository
import com.example.alertasismica.data.repository.WeatherRepository
import com.example.alertasismica.navigation.Routes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuakeDetailUiState(
    val quake: Earthquake? = null,
    val isLoadingQuake: Boolean = true,
    val weather: WeatherInfo? = null,
    val weatherLoading: Boolean = false,
    val weatherError: String? = null
)

class QuakeDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val earthquakeRepository: EarthquakeRepository,
    private val weatherRepository: WeatherRepository,
    private val savedQuakeRepository: SavedQuakeRepository
) : ViewModel() {

    private val quakeId: String = checkNotNull(savedStateHandle.get<String>(Routes.ARG_QUAKE_ID))

    private val _state = MutableStateFlow(QuakeDetailUiState())
    val state: StateFlow<QuakeDetailUiState> = _state.asStateFlow()

    val isSaved: StateFlow<Boolean> = savedQuakeRepository.observeIsSaved(quakeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    init {
        viewModelScope.launch {
            // Primero busca en los sismos descargados; si no está, en los guardados en Room
            val quake = earthquakeRepository.findCached(quakeId)
                ?: savedQuakeRepository.findByQuakeId(quakeId)?.toEarthquake()
            _state.update { it.copy(quake = quake, isLoadingQuake = false) }
            if (quake != null) loadWeather()
        }
    }

    fun loadWeather() {
        val quake = _state.value.quake ?: return
        viewModelScope.launch {
            _state.update { it.copy(weatherLoading = true, weatherError = null) }
            try {
                val weather = weatherRepository.getCurrentWeather(quake.latitude, quake.longitude)
                _state.update { it.copy(weather = weather, weatherLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(weatherLoading = false, weatherError = "No se pudo obtener el clima.")
                }
            }
        }
    }

    fun toggleSaved() {
        val quake = _state.value.quake ?: return
        viewModelScope.launch {
            if (isSaved.value) {
                savedQuakeRepository.deleteByQuakeId(quake.id)
            } else {
                savedQuakeRepository.save(quake)
            }
        }
    }
}
