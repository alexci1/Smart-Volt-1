package com.example.smartvolt.ui.view.administrador

import androidx.compose.runtime.Composable
import com.example.smartvolt.ui.components.PanelPrincipalView
import com.example.smartvolt.ui.view.operario.InicioOperarioView
import com.example.smartvolt.ui.view.supervisor.InicioSupervisorView
import com.example.smartvolt.ui.view.administrador.InicioAdministradorView
@Composable
fun InicioAdministradorView(
    onCerrarSesion: () -> Unit
) {
    PanelPrincipalView(
        nombreRol = "ADMINISTRADOR",
        titulo = "Administración de SmartVolt",
        subtitulo = "Gestiona usuarios, parámetros y recursos del sistema.",
        modulos = listOf(
            "Gestión de usuarios",
            "Gestión de roles y permisos",
            "Configuración del sistema",
            "Gestión de faenas",
            "Reportes generales",
            "Registro de actividad"
        ),
        onCerrarSesion = onCerrarSesion
    )
}
