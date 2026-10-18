package com.example.alertasismica.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.alertasismica.App

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer { RegisterViewModel(app().container.userRepository) }
        initializer { LoginViewModel(app().container.userRepository) }
        initializer { QuakeListViewModel(app().container.earthquakeRepository) }
        initializer {
            QuakeDetailViewModel(
                savedStateHandle = createSavedStateHandle(),
                earthquakeRepository = app().container.earthquakeRepository,
                weatherRepository = app().container.weatherRepository,
                savedQuakeRepository = app().container.savedQuakeRepository
            )
        }
        initializer { SavedQuakesViewModel(app().container.savedQuakeRepository) }
        initializer { ContactsViewModel(app().container.contactRepository) }
        initializer {
            ContactFormViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = app().container.contactRepository
            )
        }
        initializer { ChecklistViewModel(app().container.checklistRepository) }
    }
}

fun CreationExtras.app(): App = this[APPLICATION_KEY] as App
