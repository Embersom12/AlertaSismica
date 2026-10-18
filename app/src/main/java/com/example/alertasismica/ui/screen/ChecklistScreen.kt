package com.example.alertasismica.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alertasismica.data.local.ChecklistItemEntity
import com.example.alertasismica.ui.components.ConfirmDeleteDialog
import com.example.alertasismica.ui.components.ItemMenu
import com.example.alertasismica.ui.components.MainTopBar
import com.example.alertasismica.viewmodel.AppViewModelProvider
import com.example.alertasismica.viewmodel.ChecklistViewModel

@Composable
fun ChecklistScreen(
    onLogout: () -> Unit,
    viewModel: ChecklistViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val checklist by viewModel.items.collectAsStateWithLifecycle()
    val toDelete by viewModel.toDelete.collectAsStateWithLifecycle()

    // Diálogo de agregar / editar
    var showDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ChecklistItemEntity?>(null) }

    Scaffold(
        topBar = { MainTopBar(title = "Mochila de emergencia", onLogout = onLogout) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingItem = null
                    showDialog = true
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar elemento")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (checklist.isEmpty()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Tu mochila está vacía.\nToca + para agregar elementos.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = viewModel::addSuggested) {
                        Text("Agregar lista sugerida")
                    }
                }
            } else {
                val done = checklist.count { it.checked }
                val progress = done.toFloat() / checklist.size

                Column(modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
                    Text(
                        text = "$done de ${checklist.size} elementos listos",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(checklist, key = { it.id }) { item ->
                        ChecklistCard(
                            item = item,
                            onToggle = { viewModel.toggle(item) },
                            onEdit = {
                                editingItem = item
                                showDialog = true
                            },
                            onDelete = { viewModel.askDelete(item) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        ChecklistItemDialog(
            initial = editingItem,
            onDismiss = { showDialog = false },
            onConfirm = { nombre, cantidad ->
                val editing = editingItem
                if (editing == null) {
                    viewModel.add(nombre, cantidad)
                } else {
                    viewModel.update(editing, nombre, cantidad)
                }
                showDialog = false
            }
        )
    }

    toDelete?.let {
        ConfirmDeleteDialog(
            title = "Eliminar elemento",
            message = "¿Estás seguro de que deseas eliminar este elemento?\nEsta acción no se puede deshacer.",
            onConfirm = viewModel::confirmDelete,
            onDismiss = viewModel::dismissDelete
        )
    }
}

@Composable
private fun ChecklistCard(
    item: ChecklistItemEntity,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = item.checked, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (item.checked) TextDecoration.LineThrough else null,
                    color = if (item.checked) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Cantidad: ${item.cantidad}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ItemMenu(onEdit = onEdit, onDelete = onDelete)
        }
    }
}

@Composable
private fun ChecklistItemDialog(
    initial: ChecklistItemEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var nombre by remember { mutableStateOf(initial?.nombre.orEmpty()) }
    var cantidad by remember { mutableStateOf((initial?.cantidad ?: 1).toString()) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Nuevo elemento" else "Editar elemento") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it; error = null },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { value ->
                        cantidad = value.filter { it.isDigit() }.take(2)
                        error = null
                    },
                    label = { Text("Cantidad") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                val currentError = error
                if (currentError != null) {
                    Text(
                        text = currentError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val qty = cantidad.toIntOrNull()
                    when {
                        nombre.isBlank() -> error = "Ingresa el nombre"
                        qty == null || qty < 1 -> error = "La cantidad debe ser 1 o más"
                        else -> onConfirm(nombre, qty)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
