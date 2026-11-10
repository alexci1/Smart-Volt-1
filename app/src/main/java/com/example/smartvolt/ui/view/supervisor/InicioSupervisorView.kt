package com.example.smartvolt.ui.view.supervisor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartvolt.datos.local.AppDataBase
import com.example.smartvolt.datos.repositorio.UsuarioRepositorio
import com.example.smartvolt.modelo.Usuario
import com.example.smartvolt.ui.components.PanelPrincipalView
import com.example.smartvolt.ui.theme.AzulMedio
import com.example.smartvolt.ui.theme.AzulOscuro
import com.example.smartvolt.ui.theme.Borde
import com.example.smartvolt.ui.theme.TextoSecundario
import com.example.smartvolt.ui.theme.VerdeVolt
import com.example.smartvolt.viewmodel.UsuarioViewModel
import com.example.smartvolt.viewmodel.UsuarioViewModelFactory

@Composable
fun InicioSupervisorView(
    usuario: Usuario,
    onCerrarSesion: () -> Unit
) {
    val context = LocalContext.current

    val usuarioDAO = remember {
        AppDataBase.obtenerBaseDatos(context).usuarioDAO()
    }

    val repositorio = remember(usuarioDAO) {
        UsuarioRepositorio(usuarioDAO)
    }

    val factory = remember(repositorio) {
        UsuarioViewModelFactory(repositorio)
    }

    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = factory
    )

    val estado by usuarioViewModel.uiState.collectAsState()

    LaunchedEffect(usuario.id) {
        usuarioViewModel.observarPersonalPorSupervisor(usuario.id)
    }

    PanelPrincipalView(
        nombreRol = "SUPERVISOR",
        titulo = "Bienvenido, ${usuario.nombreCompleto}",
        subtitulo = "Controla la asistencia y las actividades de tu equipo en faena.",
        modulos = listOf(
            "Asistencia del equipo",
            "Personal a cargo",
            "Revisión de jornadas",
            "Reportes de actividad",
            "Notificaciones"
        ),
        onCerrarSesion = onCerrarSesion,
        contenidoPersonalizado = { indice, cambiarSeccion ->
            when (indice) {
                0 -> ContenidoInicioSupervisor(
                    onCambiarSeccion = cambiarSeccion
                )

                1 -> SeccionPersonalACargo(
                    personal = estado.personalACargo,
                    cargando = estado.cargandoPersonal,
                    mensajeError = estado.mensajeErrorPersonal
                )

                2 -> SeccionSupervisor(
                    titulo = "Asistencia del equipo",
                    descripcion = "Supervisa las marcaciones de entrada y salida de los trabajadores."
                )

                3 -> SeccionSupervisor(
                    titulo = "Reportes de actividad",
                    descripcion = "Consulta los reportes relacionados con las jornadas y actividades de tu equipo."
                )
            }
        }
    )
}

@Composable
fun ContenidoInicioSupervisor(
    onCambiarSeccion: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "GESTIÓN DE EQUIPO",
                color = TextoSecundario,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = "Accesos de supervisión",
                color = AzulOscuro,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        val accesos = listOf(
            Triple("Personal a cargo", "Revisa la lista de trabajadores asignados", 1),
            Triple("Asistencia del equipo", "Supervisa entradas y salidas en tiempo real", 2),
            Triple("Reportes de actividad", "Consulta informes de jornadas completadas", 3)
        )

        accesos.forEach { (titulo, descripcion, destinoIndice) ->
            TarjetaAccesoSupervisor(
                titulo = titulo,
                descripcion = descripcion,
                onClick = { onCambiarSeccion(destinoIndice) }
            )
        }
    }
}

@Composable
fun TarjetaAccesoSupervisor(
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Borde)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro
                )

                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                color = AzulOscuro,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "→",
                    color = VerdeVolt,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun SeccionSupervisor(
    titulo: String,
    descripcion: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "SUPERVISIÓN",
                color = TextoSecundario,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = titulo,
                color = AzulOscuro,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = descripcion,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, Borde)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = AzulMedio,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Funcionalidad en preparación",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = VerdeVolt
                    )
                }

                Text(
                    text = "Este módulo estará disponible próximamente para coordinar el trabajo en faena.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }
        }
    }
}

@Composable
fun SeccionPersonalACargo(
    personal: List<Usuario>,
    cargando: Boolean,
    mensajeError: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "EQUIPO DE TRABAJO",
                color = TextoSecundario,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = "Personal a cargo",
                color = AzulOscuro,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Trabajadores y operarios asignados a tu supervisión.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterHorizontally),
                color = AzulOscuro,
                strokeWidth = 3.dp
            )
        } else if (mensajeError != null) {
            Surface(
                color = Color(0xFFFFEEEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = mensajeError,
                    color = Color(0xFFBA3030),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        } else if (personal.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Borde)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Sin trabajadores asignados",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro
                    )

                    Text(
                        text = "Cuando se asignen operarios a tu equipo, aparecerán listados en esta sección.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
            }
        } else {
            Surface(
                color = AzulMedio,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${personal.size} trabajadores asignados",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = VerdeVolt,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                personal.forEach { trabajador ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Borde)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = trabajador.nombreCompleto,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulOscuro
                                )

                                Surface(
                                    color = if (trabajador.activo) Color(0xFFEBFBEE) else Color(0xFFFFEEEE),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (trabajador.activo) "ACTIVO" else "INACTIVO",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (trabajador.activo) Color(0xFF27834A) else Color(0xFFBA3030)
                                    )
                                }
                            }

                            Text(
                                text = "Cargo: ${trabajador.cargo}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundario
                            )

                            HorizontalDivider(color = Borde, thickness = 1.dp)

                            Text(
                                text = "ID: ${trabajador.id}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = TextoSecundario
                            )
                        }
                    }
                }
            }
        }
    }
}
