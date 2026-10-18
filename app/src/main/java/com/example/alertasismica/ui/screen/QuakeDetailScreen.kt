package com.example.alertasismica.ui.screen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alertasismica.ui.components.BackTopBar
import com.example.alertasismica.ui.components.LoadingBox
import com.example.alertasismica.ui.components.MagnitudeBadge
import com.example.alertasismica.ui.components.MessageBox
import com.example.alertasismica.ui.components.PrimaryButton
import com.example.alertasismica.util.Formatters
import com.example.alertasismica.viewmodel.AppViewModelProvider
import com.example.alertasismica.viewmodel.QuakeDetailViewModel

@Composable
fun QuakeDetailScreen(
    onBack: () -> Unit,
    viewModel: QuakeDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = { BackTopBar(title = "Detalle del sismo", onBack = onBack) }
    ) { padding ->
        val quake = state.quake

        when {
            state.isLoadingQuake -> LoadingBox(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            )

            quake == null -> MessageBox(
                message = "No se encontró la información de este sismo.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Encabezado
                InfoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MagnitudeBadge(magnitude = quake.magnitude, size = 72.dp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = Formatters.magnitudeLabel(quake.magnitude),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = quake.place,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Datos del sismo (API USGS)
                InfoCard {
                    Text(
                        text = "Datos del sismo",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    InfoRow("Fecha y hora", Formatters.dateTime(quake.timeMillis))
                    InfoRow("Profundidad", "${Formatters.decimal(quake.depthKm)} km")
                    InfoRow(
                        "Coordenadas",
                        "${Formatters.decimal(quake.latitude)}, ${Formatters.decimal(quake.longitude)}"
                    )
                    InfoRow("Alerta de tsunami", if (quake.tsunami) "Sí" else "No")
                }

                // Clima en el epicentro (API Open-Meteo)
                InfoCard {
                    Text(
                        text = "Clima en el epicentro",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    val weather = state.weather
                    when {
                        state.weatherLoading -> CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )

                        state.weatherError != null -> {
                            Text(
                                text = state.weatherError.orEmpty(),
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = viewModel::loadWeather) { Text("Reintentar") }
                        }

                        weather != null -> {
                            InfoRow("Estado", Formatters.weatherDescription(weather.weatherCode))
                            InfoRow("Temperatura", "${Formatters.decimal(weather.temperature)} °C")
                            InfoRow("Humedad", "${weather.humidity.toInt()} %")
                            InfoRow("Viento", "${Formatters.decimal(weather.windSpeed)} km/h")
                        }
                    }
                }

                PrimaryButton(
                    text = if (isSaved) "QUITAR DE GUARDADOS" else "GUARDAR SISMO",
                    onClick = viewModel::toggleSaved
                )

                if (quake.url.isNotBlank()) {
                    OutlinedButton(
                        onClick = {
                            try {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(quake.url))
                                )
                            } catch (e: ActivityNotFoundException) {
                                // No hay navegador instalado: no hacemos nada
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("VER EN USGS", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
