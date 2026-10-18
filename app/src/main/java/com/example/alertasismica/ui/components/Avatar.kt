package com.example.alertasismica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val avatarColors = listOf(
    Color(0xFF5B3FD4),
    Color(0xFFEF6C00),
    Color(0xFF1B7F4B),
    Color(0xFF1E5FD8),
    Color(0xFFD32F2F),
    Color(0xFFF9A825),
    Color(0xFF8E24AA),
    Color(0xFF00897B)
)

/** Círculo */
@Composable
fun Avatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    val initial = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    val color = avatarColors[(name.hashCode() and Int.MAX_VALUE) % avatarColors.size]

    Box(
        modifier = modifier
            .size(size)
            .background(color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.4f).sp
        )
    }
}
