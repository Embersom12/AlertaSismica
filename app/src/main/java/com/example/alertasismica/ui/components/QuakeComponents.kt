package com.example.alertasismica.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alertasismica.ui.theme.MagHigh
import com.example.alertasismica.ui.theme.MagLow
import com.example.alertasismica.ui.theme.MagMedium
import com.example.alertasismica.ui.theme.MagSevere
import com.example.alertasismica.util.Formatters

fun magnitudeColor(magnitude: Double): Color = when {
    magnitude < 4.0 -> MagLow
    magnitude < 5.0 -> MagMedium
    magnitude < 6.0 -> MagHigh
    else -> MagSevere
}


@Composable
fun MagnitudeBadge(
    magnitude: Double,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(magnitudeColor(magnitude), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = Formatters.magnitude(magnitude),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.34f).sp
        )
    }
}

/** Tarjeta de sismo*/
@Composable
fun QuakeCard(
    magnitude: Double,
    place: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    note: String = "",
    onClick: () -> Unit = {},
    trailing: @Composable () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MagnitudeBadge(magnitude = magnitude)
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = place,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (note.isNotBlank()) {
                    Text(
                        text = "Nota: $note",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            trailing()
        }
    }
}
