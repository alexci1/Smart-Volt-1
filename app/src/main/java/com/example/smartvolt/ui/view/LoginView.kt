package com.example.smartvolt.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.smartvolt.modelo.Usuario
import com.example.smartvolt.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    viewModel: LoginViewModel,
    onLoginExitoso: (Usuario) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
            topBar = {
                TopAppBar(title = { Text("SmartVolt - Asistencia Faena")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Acceso con ID",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Campo de texto para el ID //
                OutlinedTextField(
                    value = state.id,
                    onValueChange = {viewModel.onIdChanged(it)},
                    label = {Text("ID")},
                    placeholder = {Text("OP-001 / SU-001 / AD-001")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = {viewModel.onPasswordChanged(it)},
                    label = {Text("PASSWORD")},
                    placeholder = {Text("******")},
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    )
                )
                if (state.mensajeError != null){
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.mensajeError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.validarYIngresar { usuario -> onLoginExitoso(usuario) } },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = !state.cargando
                ) {
                    if (state.cargando){
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    }else{
                        Text("Ingresar", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }