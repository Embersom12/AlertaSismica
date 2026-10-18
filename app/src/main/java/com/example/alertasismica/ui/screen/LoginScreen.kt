package com.example.alertasismica.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alertasismica.ui.components.AppTextField
import com.example.alertasismica.ui.components.HeaderIcon
import com.example.alertasismica.ui.components.PrimaryButton
import com.example.alertasismica.viewmodel.AppViewModelProvider
import com.example.alertasismica.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.loggedIn) {
        if (state.loggedIn) onLoginSuccess()
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderIcon(icon = Icons.Default.Lock)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ingresa tus credenciales",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppTextField(
                    value = state.usuario,
                    onValueChange = viewModel::onUsuarioChange,
                    label = "Usuario",
                    leadingIcon = Icons.Default.Person
                )
                AppTextField(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Contraseña",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    imeAction = ImeAction.Done,
                    onImeAction = viewModel::login
                )
            }

            val error = state.error
            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            PrimaryButton(
                text = "INICIAR SESIÓN",
                onClick = viewModel::login,
                isLoading = state.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = "¿No tienes cuenta? ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Regístrate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onGoToRegister() }
                )
            }
        }
    }
}
