package com.example.alertasismica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alertasismica.data.local.UserEntity
import com.example.alertasismica.data.repository.SaveResult
import com.example.alertasismica.data.repository.UserRepository
import com.example.alertasismica.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val usuario: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val usuarioError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val isLoading: Boolean = false,
    val registered: Boolean = false
)

class RegisterViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onUsuarioChange(value: String) =
        _state.update { it.copy(usuario = value, usuarioError = null) }

    fun onEmailChange(value: String) =
        _state.update { it.copy(email = value, emailError = null) }

    fun onPasswordChange(value: String) =
        _state.update { it.copy(password = value, passwordError = null, confirmError = null) }

    fun onConfirmPasswordChange(value: String) =
        _state.update { it.copy(confirmPassword = value, confirmError = null) }

    fun register() {
        val current = _state.value
        if (current.isLoading) return

        val usuarioError = Validators.usuario(current.usuario)
        val emailError = Validators.email(current.email)
        val passwordError = Validators.password(current.password)
        val confirmError = when {
            current.confirmPassword.isBlank() -> "Confirma tu contraseña"
            current.confirmPassword != current.password -> "Las contraseñas no coinciden"
            else -> null
        }

        if (usuarioError != null || emailError != null || passwordError != null || confirmError != null) {
            _state.update {
                it.copy(
                    usuarioError = usuarioError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmError = confirmError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = repository.register(
                UserEntity(
                    usuario = current.usuario.trim(),
                    email = current.email.trim(),
                    password = current.password
                )
            )
            _state.update {
                when (result) {
                    SaveResult.Success -> it.copy(isLoading = false, registered = true)
                    SaveResult.UsuarioTaken ->
                        it.copy(isLoading = false, usuarioError = "Este usuario ya existe")
                    SaveResult.EmailTaken ->
                        it.copy(isLoading = false, emailError = "Este correo ya está registrado")
                }
            }
        }
    }
}
