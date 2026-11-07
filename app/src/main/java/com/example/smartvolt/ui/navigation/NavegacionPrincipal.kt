package com.example.smartvolt.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.smartvolt.ui.view.login.LoginView
import com.example.smartvolt.viewmodel.LoginViewModel
import com.example.smartvolt.ui.view.operario.InicioOperarioView
import com.example.smartvolt.ui.view.supervisor.InicioSupervisorView
import com.example.smartvolt.ui.view.administrador.InicioAdministradorView

@Composable
fun NavegacionPrincipal(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val contexto = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {

        // Login
        composable(Rutas.LOGIN) {
            LoginView(
                viewModel = loginViewModel,
                onLoginExitoso = { usuario ->

                    val rutaDestino = when (usuario.rol.trim().uppercase()) {
                        "OPERARIO" -> Rutas.INICIO_OPERARIO
                        "SUPERVISOR" -> Rutas.INICIO_SUPERVISOR
                        "ADMINISTRADOR" -> Rutas.INICIO_ADMINISTRADOR
                        else -> null
                    }

                    if (rutaDestino != null) {
                        navController.navigate(rutaDestino) {
                            popUpTo(Rutas.LOGIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        Toast.makeText(
                            contexto,
                            "El usuario no tiene un rol válido.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
        }

        composable(Rutas.INICIO_OPERARIO) {
            InicioOperarioView()
        }

        composable(Rutas.INICIO_SUPERVISOR) {
            InicioSupervisorView()
        }

        composable(Rutas.INICIO_ADMINISTRADOR) {
            InicioAdministradorView()
        }
    }
}

