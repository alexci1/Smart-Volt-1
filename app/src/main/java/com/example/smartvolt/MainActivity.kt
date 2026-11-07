
package com.example.smartvolt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.smartvolt.datos.local.AppDataBase
import com.example.smartvolt.datos.seguridad.GestorContrasenas
import com.example.smartvolt.modelo.Usuario
import com.example.smartvolt.ui.theme.SmartVoltTheme
import com.example.smartvolt.ui.view.login.LoginView
import com.example.smartvolt.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.Toast
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDataBase.obtenerBaseDatos(applicationContext)
        val usuarioDAO = database.usuarioDAO()

        val loginViewModel = LoginViewModel(usuarioDAO)

        lifecycleScope.launch(Dispatchers.IO) {

            val usuarioExistente =
                usuarioDAO.obtenerUsuarioPorId("OP-001")

            if (usuarioExistente == null) {

                val usuarioPrueba = Usuario(
                    id = "OP-001",
                    contrasenaHash = GestorContrasenas.generarHash("123456"),
                    nombreCompleto = "Juan Pérez",
                    cargo = "Operador",
                    rol = "OPERARIO"
                )

                usuarioDAO.agregarUsuario(usuarioPrueba)
            }
        }

        setContent {
            SmartVoltTheme {
                LoginView(
                    viewModel = loginViewModel,
                    onLoginExitoso = { usuario ->
                        Toast.makeText(
                            this@MainActivity,
                            "¡Bienvenido, ${usuario.nombreCompleto}!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        }
    }
}
