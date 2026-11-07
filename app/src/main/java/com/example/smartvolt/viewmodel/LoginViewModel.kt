package com.example.smartvolt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartvolt.datos.local.dao.UsuarioDAO
import com.example.smartvolt.datos.seguridad.GestorContrasenas
import com.example.smartvolt.modelo.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val id: String = "",
    val password: String = "",
    val cargando: Boolean = false,
    val mensajeError: String? = null,
    val usuarioLogeado: Usuario? = null
)

class LoginViewModel(
    private val usuarioDAO: UsuarioDAO
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onIdChanged(nuevoId: String) {
        _uiState.update {
            it.copy(
                id = nuevoId,
                mensajeError = null
            )
        }
    }

    fun onPasswordChanged(nuevoPassword: String) {
        if (nuevoPassword.length <= 6 && nuevoPassword.all { it.isDigit() }) {
            _uiState.update {
                it.copy(
                    password = nuevoPassword,
                    mensajeError = null
                )
            }
        }
    }

    fun validarYIngresar(onExito: (Usuario) -> Unit) {

        val idActual = _uiState.value.id.trim()
        val passwordActual = _uiState.value.password

        if (idActual.isEmpty() || passwordActual.isEmpty()) {
            _uiState.update {
                it.copy(mensajeError = "Por favor, ingresa tu ID y contraseña.")
            }
            return
        }

        if (passwordActual.length < 6) {
            _uiState.update {
                it.copy(mensajeError = "La contraseña debe tener al menos 6 caracteres.")
            }
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(cargando = true, mensajeError = null)
            }

            try {
                val usuario = usuarioDAO.obtenerUsuarioPorId(idActual)

                if (
                    usuario != null &&
                    usuario.activo &&
                    GestorContrasenas.verificar(
                        passwordActual,
                        usuario.contrasenaHash
                    )
                ) {
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            usuarioLogeado = usuario
                        )
                    }

                    onExito(usuario)

                } else {
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            mensajeError = "Credenciales inválidas o usuario no registrado."
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        mensajeError = "Error de sistema. Contacta con un administrador."
                    )
                }
            }
        }
    }

    fun cerrarSesion() {
        _uiState.value = LoginUiState()
    }

}
