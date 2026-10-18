package com.example.alertasismica.ui.screen

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alertasismica.ui.components.AppTextField
import com.example.alertasismica.ui.components.HeaderIcon
import com.example.alertasismica.ui.components.PrimaryButton
import com.example.alertasismica.viewmodel.AppViewModelProvider
import com.example.alertasismica.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onGoToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.registered) {
        if (state.registered) {
            Toast.makeText(context, "Cuenta creada. Ya puedes iniciar sesión", Toast.LENGTH_SHORT).show()
            onRegistered()
        }
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
            HeaderIcon(icon = Icons.Default.PersonAdd)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Completa tus datos para registrarte",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppTextField(
                    value = state.usuario,
                    onValueChange = viewModel::onUsuarioChange,
                    label = "Usuario",
                    leadingIcon = Icons.Default.Person,
                    error = state.usuarioError
                )
                AppTextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Correo electrónico",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    error = state.emailError
                )
                AppTextField(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Contraseña",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    error = state.passwordError
                )
                AppTextField(
                    value = state.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = "Confirmar contraseña",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    error = state.confirmError,
                    imeAction = ImeAction.Done,
                    onImeAction = viewModel::register
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            PrimaryButton(
                text = "REGISTRARME",
                onClick = viewModel::register,
                isLoading = state.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Inicia sesión",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onGoToLogin() }
                )
            }
        }
    }
}
