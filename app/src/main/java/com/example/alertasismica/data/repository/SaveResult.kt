package com.example.alertasismica.data.repository

sealed interface SaveResult {
    data object Success : SaveResult
    data object UsuarioTaken : SaveResult
    data object EmailTaken : SaveResult
}
