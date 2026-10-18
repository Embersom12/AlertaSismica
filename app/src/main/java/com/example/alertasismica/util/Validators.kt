package com.example.alertasismica.util

import android.util.Patterns

/** validaciones de cada cosa */
object Validators {

    fun usuario(value: String): String? = when {
        value.isBlank() -> "Ingresa un usuario"
        value.trim().length < 3 -> "Mínimo 3 caracteres"
        value.trim().contains(' ') -> "No puede tener espacios"
        else -> null
    }

    fun email(value: String): String? = when {
        value.isBlank() -> "Ingresa tu correo electrónico"
        !Patterns.EMAIL_ADDRESS.matcher(value.trim()).matches() -> "Correo electrónico no válido"
        else -> null
    }

    fun password(value: String): String? = when {
        value.isBlank() -> "Ingresa una contraseña"
        value.length < 6 -> "Mínimo 6 caracteres"
        else -> null
    }

    fun nombre(value: String): String? =
        if (value.isBlank()) "Ingresa el nombre" else null

    fun telefono(value: String): String? {
        val clean = cleanPhone(value)
        return when {
            clean.isBlank() -> "Ingresa el teléfono"
            !Regex("^\\+?[0-9]{7,15}$").matches(clean) -> "Teléfono no válido (7 a 15 dígitos)"
            else -> null
        }
    }

    fun cleanPhone(value: String): String = value.filter { it.isDigit() || it == '+' }
}
