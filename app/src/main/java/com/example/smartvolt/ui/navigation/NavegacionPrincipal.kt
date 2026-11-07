package com.example.smartvolt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavegacionPrincipal(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {

        composable(Rutas.LOGIN) {
            // Próximamente conectaremos LoginView
        }

        composable(Rutas.INICIO_OPERARIO) {
            // Próximamente conectaremos InicioOperarioView
        }

        composable(Rutas.INICIO_SUPERVISOR) {
            // Próximamente conectaremos InicioSupervisorView
        }

        composable(Rutas.INICIO_ADMINISTRADOR) {
            // Próximamente conectaremos InicioAdministradorView
        }
    }
}