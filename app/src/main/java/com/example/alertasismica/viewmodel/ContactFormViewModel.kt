package com.example.alertasismica.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alertasismica.data.local.ContactEntity
import com.example.alertasismica.data.repository.ContactRepository
import com.example.alertasismica.navigation.Routes
import com.example.alertasismica.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ContactFormState(
    val nombre: String = "",
    val telefono: String = "",
    val relacion: String = "",
    val nombreError: String? = null,
    val telefonoError: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val done: Boolean = false,
    val doneMessage: String = ""
)

class ContactFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ContactRepository
) : ViewModel() {

    private val contactId: Int? = savedStateHandle.get<Int>(Routes.ARG_CONTACT_ID)
    val isEditing: Boolean = contactId != null

    private val _state = MutableStateFlow(ContactFormState(isLoading = contactId != null))
    val state: StateFlow<ContactFormState> = _state.asStateFlow()

    init {
        if (contactId != null) {
            viewModelScope.launch {
                val contact = repository.getContact(contactId)
                if (contact == null) {
                    _state.update {
                        it.copy(isLoading = false, done = true, doneMessage = "Contacto no encontrado")
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            nombre = contact.nombre,
                            telefono = contact.telefono,
                            relacion = contact.relacion
                        )
                    }
                }
            }
        }
    }

    fun onNombreChange(value: String) =
        _state.update { it.copy(nombre = value, nombreError = null) }

    fun onTelefonoChange(value: String) =
        _state.update { it.copy(telefono = value, telefonoError = null) }

    fun onRelacionChange(value: String) =
        _state.update { it.copy(relacion = value) }

    fun save() {
        val current = _state.value
        if (current.isSaving) return

        val nombreError = Validators.nombre(current.nombre)
        val telefonoError = Validators.telefono(current.telefono)
        if (nombreError != null || telefonoError != null) {
            _state.update { it.copy(nombreError = nombreError, telefonoError = telefonoError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val contact = ContactEntity(
                id = contactId ?: 0,
                nombre = current.nombre.trim(),
                telefono = Validators.cleanPhone(current.telefono),
                relacion = current.relacion.trim()
            )
            if (contactId == null) repository.insert(contact) else repository.update(contact)
            _state.update {
                it.copy(
                    isSaving = false,
                    done = true,
                    doneMessage = if (contactId == null) "Contacto guardado" else "Contacto actualizado"
                )
            }
        }
    }
}
