
package com.example.smartvolt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartvolt.datos.repositorio.UsuarioRepositorio
import com.example.smartvolt.modelo.Usuario
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

data class UsuarioUiState(
    val usuarios: List<Usuario> = emptyList(),
    val personalACargo: List<Usuario> = emptyList(),
    val cargando: Boolean = false,
    val cargandoPersonal: Boolean = false,
    val usuarioActualizandoId: String? = null,
    val mensaje: String? = null,
    val mensajeError: String? = null,
    val mensajeErrorPersonal: String? = null
)

class UsuarioViewModel(
    private val repositorio: UsuarioRepositorio
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    // Evita actualizaciones simultáneas de usuarios.
    private val bloqueoActualizacion = Mutex()

    init {
        observarUsuarios()
    }

    // Observar los cambios en la lista de todos los usuarios.
    private fun observarUsuarios() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                cargando = true,
                mensajeError = null
            )

            try {
                repositorio.listarUsuarios().collect { usuarios ->
                    _uiState.value = _uiState.value.copy(
                        usuarios = usuarios,
                        cargando = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    mensajeError = "No fue posible cargar los usuarios."
                )
            }
        }
    }

    // Observar el personal asignado a un supervisor.
    fun observarPersonalPorSupervisor(supervisorId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                cargandoPersonal = true,
                mensajeErrorPersonal = null
            )

            try {
                repositorio.listarPersonalPorSupervisor(
                    supervisorId
                ).collect { personal ->
                    _uiState.value = _uiState.value.copy(
                        personalACargo = personal,
                        cargandoPersonal = false,
                        mensajeErrorPersonal = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargandoPersonal = false,
                    mensajeErrorPersonal =
                        "No fue posible cargar el personal a cargo."
                )
            }
        }
    }

    // Activar o desactivar un usuario.
    fun actualizarEstadoUsuario(
        id: String,
        activo: Boolean
    ) {
        // Evitar operaciones simultáneas.
        if (!bloqueoActualizacion.tryLock()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                usuarioActualizandoId = id,
                mensaje = null,
                mensajeError = null
            )

            try {
                repositorio.actualizarEstadoUsuario(
                    id = id,
                    activo = activo
                )

                _uiState.value = _uiState.value.copy(
                    mensaje = if (activo) {
                        "Usuario activado correctamente."
                    } else {
                        "Usuario desactivado correctamente."
                    },
                    mensajeError = null
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    mensaje = null,
                    mensajeError =
                        "No fue posible actualizar el estado del usuario."
                )

            } finally {
                _uiState.value = _uiState.value.copy(
                    usuarioActualizandoId = null
                )

                bloqueoActualizacion.unlock()
            }
        }
    }

    // Limpiar mensajes de la interfaz.
    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(
            mensaje = null,
            mensajeError = null
        )
    }
}
