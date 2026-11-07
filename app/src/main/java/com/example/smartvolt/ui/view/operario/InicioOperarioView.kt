
package com.example.smartvolt.ui.view.operario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartvolt.ui.components.PanelPrincipalView
import com.example.smartvolt.modelo.Usuario

@Composable
fun InicioOperarioView(
    usuario: Usuario,
    onCerrarSesion: () -> Unit
) {
    PanelPrincipalView(
        nombreRol = "OPERARIO",
        titulo = "Bienvenido al área operativa",
        subtitulo = "Gestiona tu jornada y consulta tu asistencia.",
        modulos = listOf(
            "Marcar asistencia",
            "Mi jornada laboral",
            "Historial de asistencia",
            "Mis notificaciones"
        ),
        onCerrarSesion = onCerrarSesion,

        contenidoPersonalizado = { indice ->

            when (indice) {

                0 -> {
                    ContenidoInicioOperario()
                }

                1 -> {
                    AsistenciaOperarioView()
                }

                2 -> {
                    HistorialOperarioView()
                }

                3 -> PerfilOperarioView(usuario)
            }
        }
    )
}

@Composable
private fun ContenidoInicioOperario() {

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "SMARTVOLT",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "Bienvenido al área operativa",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "Gestiona tu jornada y consulta tu asistencia.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Text(
            text = "Accesos rápidos",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        val accesos = listOf(
            "Control de asistencia",
            "Historial de jornadas",
            "Mi perfil"
        )

        accesos.forEach { acceso ->

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = acceso,
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
