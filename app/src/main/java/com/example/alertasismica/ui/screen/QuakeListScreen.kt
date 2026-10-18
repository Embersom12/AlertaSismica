package com.example.alertasismica.ui.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alertasismica.ui.components.LoadingBox
import com.example.alertasismica.ui.components.MainTopBar
import com.example.alertasismica.ui.components.MessageBox
import com.example.alertasismica.ui.components.QuakeCard
import com.example.alertasismica.util.Formatters
import com.example.alertasismica.viewmodel.AppViewModelProvider
import com.example.alertasismica.viewmodel.MagnitudeFilter
import com.example.alertasismica.viewmodel.QuakeListViewModel

@Composable
fun QuakeListScreen(
    onQuakeClick: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: QuakeListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            MainTopBar(
                title = "Sismos recientes",
                onLogout = onLogout,
                actions = {
                    IconButton(onClick = viewModel::load) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Datos de los últimos 30 días (USGS)",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 4.dp)
            )

            // Filtros
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.onlyPeru,
                    onClick = { viewModel.onOnlyPeruChange(!state.onlyPeru) },
                    label = { Text("Perú y alrededores") }
                )
                MagnitudeFilter.entries.forEach { filter ->
                    FilterChip(
                        selected = state.filter == filter,
                        onClick = { viewModel.onFilterChange(filter) },
                        label = { Text(filter.label) }
                    )
                }
            }

            val error = state.errorMessage
            when {
                state.isLoading -> LoadingBox(Modifier.weight(1f))

                error != null -> MessageBox(
                    message = error,
                    modifier = Modifier.weight(1f),
                    actionLabel = "Reintentar",
                    onAction = viewModel::load
                )

                state.quakes.isEmpty() -> MessageBox(
                    message = "No hay sismos con estos filtros.\nPrueba con otra magnitud.",
                    modifier = Modifier.weight(1f)
                )

                else -> LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.quakes, key = { it.id }) { quake ->
                        QuakeCard(
                            magnitude = quake.magnitude,
                            place = quake.place,
                            subtitle = "${Formatters.relativeTime(quake.timeMillis)} · " +
                                "${Formatters.decimal(quake.depthKm)} km de profundidad",
                            onClick = { onQuakeClick(quake.id) }
                        )
                    }
                }
            }
        }
    }
}
