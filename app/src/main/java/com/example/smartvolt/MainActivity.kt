package com.example.smartvolt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.smartvolt.modelo.Usuario
import com.example.smartvolt.ui.theme.SmartVoltTheme
import com.example.smartvolt.ui.view.login.LoginView
import com.example.smartvolt.datos.local.AppDataBase
import com.example.smartvolt.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDataBase.obtenerBaseDatos(applicationContext)
        val usuarioDAO = database.usuarioDAO()

        lifecycleScope.launch(Dispatchers.IO){
            val usuarioPrueba = Usuario(
                id = "OP-001",
                password = "123456",
                nombreCompleto = "Juan Pérez",
                cargo = "Operador"
            )
            usuarioDAO.agregarUsuario(usuarioPrueba)
        }

        val loginViewModel = LoginViewModel(usuarioDAO)

        setContent {
            SmartVoltTheme {
                LoginView(
                    viewModel = loginViewModel,
                    onLoginExitoso = { usuario -> }
                )
            }
        }

    }
}
