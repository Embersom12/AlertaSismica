package com.example.alertasismica.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alertasismica.data.local.SavedQuakeEntity
import com.example.alertasismica.ui.components.ConfirmDeleteDialog
import com.example.alertasismica.ui.components.ItemMenu
import com.example.alertasismica.ui.components.MainTopBar
import com.example.alertasismica.ui.components.MessageBox
import com.example.alertasismica.ui.components.QuakeCard
import com.example.alertasismica.ui.components.SearchField
import com.example.alertasismica.util.Formatters
import com.example.alertasismica.viewmodel.AppViewModelProvider
import com.example.alertasismica.viewmodel.SavedQuakesViewModel

@Composable
fun SavedQuakesScreen(
    onQuakeClick: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: SavedQuakesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val quakes by viewModel.quakes.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val toDelete by viewModel.toDelete.collectAsStateWithLifecycle()
    val toEditNote by viewModel.toEditNote.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { MainTopBar(title = "Sismos guardados", onLogout = onLogout) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SearchField(
                query = query,
                onQueryChange = viewModel::onQueryChange,
                placeholder = "Buscar por lugar o nota"
            )

            Text(
                text = "Sismos que guardaste en tu dispositivo",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )

            if (quakes.isEmpty()) {
                MessageBox(
                    message = if (query.isBlank()) {
                        "Aún no guardas sismos.\nAbre uno desde la pestaña Sismos y toca Guardar."
                    } else {
                        "No se encontraron sismos con esa búsqueda."
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(quakes, key = { it.id }) { item ->
                        QuakeCard(
                            magnitude = item.magnitude,
                            place = item.place,
                            subtitle = Formatters.dateTime(item.timeMillis),
                            note = item.note,
                            onClick = { onQuakeClick(item.quakeId) },
                            trailing = {
                                ItemMenu(
                                    editLabel = "Editar nota",
                                    onEdit = { viewModel.askEditNote(item) },
                                    onDelete = { viewModel.askDelete(item) }
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    toDelete?.let {
        ConfirmDeleteDialog(
            title = "Eliminar sismo guardado",
            message = "¿Deseas quitar este sismo de tus guardados?\nEsta acción no se puede deshacer.",
            onConfirm = viewModel::confirmDelete,
            onDismiss = viewModel::dismissDelete
        )
    }

    toEditNote?.let { item ->
        NoteDialog(
            item = item,
            onSave = viewModel::saveNote,
            onDismiss = viewModel::dismissEditNote
        )
    }
}

@Composable
private fun NoteDialog(
    item: SavedQuakeEntity,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var note by remember { mutableStateOf(item.note) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nota personal") },
        text = {
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Escribe una nota") },
                modifier = Modifier.padding(top = 4.dp)
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave(note) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
