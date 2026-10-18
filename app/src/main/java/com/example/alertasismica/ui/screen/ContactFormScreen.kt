package com.example.alertasismica.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alertasismica.ui.components.AppTextField
import com.example.alertasismica.ui.components.BackTopBar
import com.example.alertasismica.ui.components.LoadingBox
import com.example.alertasismica.ui.components.PrimaryButton
import com.example.alertasismica.viewmodel.AppViewModelProvider
import com.example.alertasismica.viewmodel.ContactFormViewModel

@Composable
fun ContactFormScreen(
    onBack: () -> Unit,
    viewModel: ContactFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.done) {
        if (state.done) {
            Toast.makeText(context, state.doneMessage, Toast.LENGTH_SHORT).show()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = if (viewModel.isEditing) "Editar contacto" else "Nuevo contacto",
                onBack = onBack
            )
        }
    ) { padding ->
        if (state.isLoading) {
            LoadingBox(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppTextField(
                        value = state.nombre,
                        onValueChange = viewModel::onNombreChange,
                        label = "Nombre",
                        leadingIcon = Icons.Default.Person,
                        error = state.nombreError
                    )
                    AppTextField(
                        value = state.telefono,
                        onValueChange = viewModel::onTelefonoChange,
                        label = "Teléfono",
                        leadingIcon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone,
                        error = state.telefonoError
                    )
                    AppTextField(
                        value = state.relacion,
                        onValueChange = viewModel::onRelacionChange,
                        label = "Relación (mamá, amigo, vecino...)",
                        leadingIcon = Icons.Default.Group,
                        imeAction = ImeAction.Done,
                        onImeAction = viewModel::save
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                PrimaryButton(
                    text = if (viewModel.isEditing) "ACTUALIZAR" else "GUARDAR",
                    onClick = viewModel::save,
                    isLoading = state.isSaving
                )
            }
        }
    }
}
