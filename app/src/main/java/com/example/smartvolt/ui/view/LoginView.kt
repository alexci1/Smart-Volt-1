
package com.example.smartvolt.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.smartvolt.modelo.Usuario
import com.example.smartvolt.viewmodel.LoginViewModel

private val AzulOscuro = Color(0xFF101D32)
private val AzulMedio = Color(0xFF1D304A)
private val VerdeVolt = Color(0xFFD6F36A)
private val Fondo = Color(0xFFF1F3F6)
private val TextoSecundario = Color(0xFF718096)
private val Borde = Color(0xFFDCE2E9)

@Composable
fun LoginView(
    viewModel: LoginViewModel,
    onLoginExitoso: (Usuario) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {

        // ENCABEZADO INSTITUCIONAL

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AzulOscuro)
                .padding(
                    horizontal = 26.dp,
                    vertical = 30.dp
                )
        ) {

            // MARCA

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚡",
                    color = VerdeVolt,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "SMARTVOLT",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "GESTIÓN DE FAENAS",
                        color = Color(0xFFB5C2D3),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "LOGIN",
                    color = VerdeVolt,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            HorizontalDivider(
                color = Color(0xFF405067),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // MENSAJE PRINCIPAL

            Text(
                text = "PLATAFORMA DE OPERACIONES",
                color = VerdeVolt,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tu jornada\ncomienza aquí.",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                lineHeight = MaterialTheme.typography.headlineLarge.lineHeight
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sistema de control y gestión de asistencia en faenas.",
                color = Color(0xFFB5C2D3),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // INFORMACIÓN DE SEGURIDAD

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    color = AzulMedio,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "✓",
                        color = VerdeVolt,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 9.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Acceso seguro",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Identificación de personal autorizado",
                        color = Color(0xFFB5C2D3),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // FORMULARIO

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 26.dp,
                    vertical = 30.dp
                )
        ) {

            Text(
                text = "IDENTIFICACIÓN DE USUARIO",
                color = TextoSecundario,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Iniciar sesión",
                color = AzulOscuro,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingresa tus credenciales para acceder a la plataforma.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(28.dp))

            // IDENTIFICADOR

            Text(
                text = "ID de trabajador",
                color = AzulOscuro,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.id,
                onValueChange = {
                    viewModel.onIdChanged(it)
                },
                placeholder = {
                    Text("Ej. OP-001")
                },
                leadingIcon = {
                    Text(
                        text = "ID",
                        color = TextoSecundario,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulOscuro,
                    unfocusedBorderColor = Borde,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = AzulOscuro
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // CONTRASEÑA

            Text(
                text = "Contraseña",
                color = AzulOscuro,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    viewModel.onPasswordChanged(it)
                },
                placeholder = {
                    Text("Ingresa tu contraseña")
                },
                leadingIcon = {
                    Text(
                        text = "*",
                        color = TextoSecundario,
                        fontWeight = FontWeight.Bold
                    )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulOscuro,
                    unfocusedBorderColor = Borde,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = AzulOscuro
                )
            )

            // ERROR DE AUTENTICACIÓN

            if (state.mensajeError != null) {
                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    color = Color(0xFFFFEEEE),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.mensajeError!!,
                        color = Color(0xFFBA3030),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // BOTÓN DE ACCESO

            Button(
                onClick = {
                    viewModel.validarYIngresar { usuario ->
                        onLoginExitoso(usuario)
                    }
                },
                enabled = !state.cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulOscuro,
                    contentColor = Color.White,
                    disabledContainerColor = AzulMedio
                )
            ) {

                if (state.cargando) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = VerdeVolt,
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = "Ingresar",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "→",
                        color = VerdeVolt,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            HorizontalDivider(
                color = Borde,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // PIE DE PÁGINA

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SMARTVOLT v1.0",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "GESTIÓN DE FAENAS",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
