package com.example.alertasismica.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {

    fun dateTime(millis: Long): String =
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-PE")).format(Date(millis))

    fun relativeTime(millis: Long): String {
        val minutes = (System.currentTimeMillis() - millis) / 60_000
        return when {
            minutes < 1 -> "justo ahora"
            minutes < 60 -> "hace $minutes min"
            minutes < 60 * 24 -> "hace ${minutes / 60} h"
            else -> "hace ${minutes / (60 * 24)} días"
        }
    }

    fun magnitude(value: Double): String = String.format(Locale.US, "%.1f", value)

    fun decimal(value: Double): String = String.format(Locale.US, "%.1f", value)

    fun magnitudeLabel(value: Double): String = when {
        value < 3.0 -> "Muy leve"
        value < 4.0 -> "Leve"
        value < 5.0 -> "Moderado"
        value < 6.0 -> "Fuerte"
        value < 7.0 -> "Muy fuerte"
        else -> "Severo"
    }

    /** Códigos WMO de clima que devuelve Open-Meteo. */
    fun weatherDescription(code: Int): String = when (code) {
        0 -> "Despejado"
        1, 2, 3 -> "Parcialmente nublado"
        45, 48 -> "Niebla"
        in 51..57 -> "Llovizna"
        in 61..67 -> "Lluvia"
        in 71..77 -> "Nieve"
        in 80..82 -> "Chubascos"
        85, 86 -> "Chubascos de nieve"
        in 95..99 -> "Tormenta"
        else -> "Sin datos"
    }
}
