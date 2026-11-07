package com.example.smartvolt.ui.view.operario

import androidx.compose.runtime.Composable
import com.example.smartvolt.ui.components.PanelPrincipalView

@Composable
fun InicioOperarioView(
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
        onCerrarSesion = onCerrarSesion
    )
}
