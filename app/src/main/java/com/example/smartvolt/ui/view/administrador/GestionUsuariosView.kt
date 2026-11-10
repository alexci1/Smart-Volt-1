package com.example.smartvolt.ui.view.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartvolt.modelo.Usuario
import com.example.smartvolt.ui.theme.AzulMedio
import com.example.smartvolt.ui.theme.AzulOscuro
import com.example.smartvolt.ui.theme.Borde
import com.example.smartvolt.ui.theme.TextoSecundario
import com.example.smartvolt.ui.theme.VerdeVolt
import com.example.smartvolt.viewmodel.UsuarioViewModel

@Composable
fun GestionUsuariosView(
    usuarioViewModel: UsuarioViewModel
) {
    val estado by usuarioViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "ADMINISTRACIÓN",
                color = TextoSecundario,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = "Gestión de usuarios",
                color = AzulOscuro,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Administra las cuentas, cargos y estado de acceso del personal.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        estado.mensaje?.let { mensaje ->
            Surface(
                color = Color(0xFFEBFBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = mensaje,
                    color = Color(0xFF27834A),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        estado.mensajeError?.let { error ->
            Surface(
                color = Color(0xFFFFEEEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = error,
                    color = Color(0xFFBA3030),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        when {
            estado.cargando && estado.usuarios.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.CenterHorizontally),
                    color = AzulOscuro,
                    strokeWidth = 3.dp
                )
            }

            estado.usuarios.isEmpty() -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Borde)
                ) {
                    Text(
                        text = "No hay usuarios registrados en el sistema.",
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
            }

            else -> {
                Surface(
                    color = AzulMedio,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Usuarios registrados: ${estado.usuarios.size}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = VerdeVolt,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    estado.usuarios.forEach { usuario ->
                        TarjetaUsuario(
                            usuario = usuario,
                            actualizando = estado.usuarioActualizandoId == usuario.id,
                            onCambiarEstado = { activo ->
                                usuarioViewModel.actualizarEstadoUsuario(
                                    id = usuario.id,
                                    activo = activo
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaUsuario(
    usuario: Usuario,
    actualizando: Boolean,
    onCambiarEstado: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Borde)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = usuario.nombreCompleto,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro
                    )

                    Surface(
                        color = if (usuario.activo) Color(0xFFEBFBEE) else Color(0xFFFFEEEE),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (usuario.activo) "ACTIVA" else "INACTIVA",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (usuario.activo) Color(0xFF27834A) else Color(0xFFBA3030)
                        )
                    }
                }

                Text(
                    text = "ID: ${usuario.id} | Cargo: ${usuario.cargo} | Rol: ${usuario.rol}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }

            if (actualizando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = AzulOscuro,
                    strokeWidth = 2.dp
                )
            } else {
                Switch(
                    checked = usuario.activo,
                    onCheckedChange = onCambiarEstado,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = VerdeVolt,
                        checkedTrackColor = AzulOscuro,
                        uncheckedThumbColor = TextoSecundario,
                        uncheckedTrackColor = Borde
                    )
                )
            }
        }
    }
}
