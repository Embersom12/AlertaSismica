package com.example.alertasismica.ui.screen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alertasismica.data.local.ContactEntity
import com.example.alertasismica.ui.components.Avatar
import com.example.alertasismica.ui.components.ConfirmDeleteDialog
import com.example.alertasismica.ui.components.ItemMenu
import com.example.alertasismica.ui.components.MainTopBar
import com.example.alertasismica.ui.components.MessageBox
import com.example.alertasismica.ui.components.SearchField
import com.example.alertasismica.viewmodel.AppViewModelProvider
import com.example.alertasismica.viewmodel.ContactsViewModel

@Composable
fun ContactsScreen(
    onAddContact: () -> Unit,
    onEditContact: (Int) -> Unit,
    onLogout: () -> Unit,
    viewModel: ContactsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val toDelete by viewModel.toDelete.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = { MainTopBar(title = "Contactos de emergencia", onLogout = onLogout) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddContact,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo contacto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SearchField(
                query = query,
                onQueryChange = viewModel::onQueryChange,
                placeholder = "Buscar contacto"
            )

            Text(
                text = "Personas a quienes avisar en una emergencia",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )

            if (contacts.isEmpty()) {
                MessageBox(
                    message = if (query.isBlank()) {
                        "Aún no tienes contactos.\nToca + para agregar uno."
                    } else {
                        "No se encontraron contactos."
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(contacts, key = { it.id }) { contact ->
                        ContactCard(
                            contact = contact,
                            onCall = {
                                try {
                                    context.startActivity(
                                        Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.telefono}"))
                                    )
                                } catch (e: ActivityNotFoundException) {
                                    // El dispositivo no puede marcar: no hacemos nada
                                }
                            },
                            onEdit = { onEditContact(contact.id) },
                            onDelete = { viewModel.askDelete(contact) }
                        )
                    }
                }
            }
        }
    }

    toDelete?.let {
        ConfirmDeleteDialog(
            title = "Eliminar contacto",
            message = "¿Estás seguro de que deseas eliminar este contacto?\nEsta acción no se puede deshacer.",
            onConfirm = viewModel::confirmDelete,
            onDismiss = viewModel::dismissDelete
        )
    }
}

@Composable
private fun ContactCard(
    contact: ContactEntity,
    onCall: () -> Unit,
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
            modifier = Modifier.padding(start = 12.dp, top = 10.dp, bottom = 10.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(name = contact.nombre)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = contact.telefono,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (contact.relacion.isNotBlank()) {
                    Text(
                        text = contact.relacion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            IconButton(onClick = onCall) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Llamar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            ItemMenu(onEdit = onEdit, onDelete = onDelete)
        }
    }
}
