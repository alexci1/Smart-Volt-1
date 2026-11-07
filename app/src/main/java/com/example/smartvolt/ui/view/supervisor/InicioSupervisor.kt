package com.example.smartvolt.ui.view.supervisor

import androidx.compose.runtime.Composable
import com.example.smartvolt.ui.components.PanelPrincipalView


@Composable
fun InicioSupervisorView() {
    PanelPrincipalView(
        nombreRol = "SUPERVISOR",
        titulo = "Panel de supervisión",
        subtitulo = "Controla la asistencia y las actividades de tu equipo.",
        modulos = listOf(
            "Asistencia del equipo",
            "Personal a cargo",
            "Revisión de jornadas",
            "Reportes de actividad",
            "Notificaciones"
        )
    )
}
