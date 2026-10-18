package com.example.alertasismica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alertasismica.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val usuario: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val loggedIn: Boolean = false
)

class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onUsuarioChange(value: String) =
        _state.update { it.copy(usuario = value, error = null) }

    fun onPasswordChange(value: String) =
        _state.update { it.copy(password = value, error = null) }

    fun login() {
        val current = _state.value
        if (current.isLoading) return

        if (current.usuario.isBlank() || current.password.isBlank()) {
            _state.update { it.copy(error = "Ingresa tu usuario y contraseña") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val user = repository.login(current.usuario, current.password)
            _state.update {
                if (user != null) {
                    it.copy(isLoading = false, loggedIn = true)
                } else {
                    it.copy(isLoading = false, error = "Usuario o contraseña incorrectos")
                }
            }
        }
    }
}
