
package com.example.smartvolt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartvolt.datos.repositorio.UsuarioRepositorio
import com.example.smartvolt.datos.seguridad.GestorContrasenas
import com.example.smartvolt.modelo.Usuario
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class UsuarioUiState(
    val usuarios: List<Usuario> = emptyList(),
    val personalACargo: List<Usuario> = emptyList(),
    val cargando: Boolean = false,
    val cargandoPersonal: Boolean = false,
    val usuarioActualizandoId: String? = null,
    val creandoUsuario: Boolean = false,
    val mensaje: String? = null,
    val mensajeError: String? = null,
    val mensajeErrorPersonal: String? = null
)

class UsuarioViewModel(
    private val repositorio: UsuarioRepositorio
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private val bloqueoCreacion = Mutex()
    private val bloqueoActualizacion = Mutex()

    init {
        observarUsuarios()
    }

    // --------------------------------------------------
    // LISTAR USUARIOS
    // --------------------------------------------------

    private fun observarUsuarios() {
        viewModelScope.launch {
            repositorio.listarUsuarios().collect { usuarios ->
                _uiState.value = _uiState.value.copy(
                    usuarios = usuarios
                )
            }
        }
    }

    // --------------------------------------------------
    // CREAR USUARIO
    // --------------------------------------------------

    fun crearUsuario(
        id: String,
        nombreCompleto: String,
        contrasena: String,
        cargo: String,
        rol: String,
        supervisorId: String? = null
    ) {
        viewModelScope.launch {
            if (!bloqueoCreacion.tryLock()) return@launch

            try {
                _uiState.value = _uiState.value.copy(
                    creandoUsuario = true,
                    mensaje = null,
                    mensajeError = null
                )

                val idLimpio = id.trim().uppercase()
                val nombreLimpio = nombreCompleto.trim()
                val cargoLimpio = cargo.trim()
                val rolLimpio = rol.trim().uppercase()
                val supervisorLimpio = supervisorId
                    ?.trim()
                    ?.uppercase()
                    ?.takeIf { it.isNotBlank() }

                // Validar campos obligatorios.
                if (
                    idLimpio.isBlank() ||
                    nombreLimpio.isBlank() ||
                    contrasena.isBlank() ||
                    cargoLimpio.isBlank() ||
                    rolLimpio.isBlank()
                ) {
                    _uiState.value = _uiState.value.copy(
                        mensajeError = "Completa todos los campos obligatorios."
                    )
                    return@launch
                }

                // Validar longitud de contraseña.
                if (contrasena.length < 6) {
                    _uiState.value = _uiState.value.copy(
                        mensajeError = "La contraseña debe tener al menos 6 caracteres."
                    )
                    return@launch
                }

                // Validar rol.
                val rolesPermitidos = listOf(
                    "OPERARIO",
                    "SUPERVISOR",
                    "ADMINISTRADOR"
                )

                if (rolLimpio !in rolesPermitidos) {
                    _uiState.value = _uiState.value.copy(
                        mensajeError = "El rol seleccionado no es válido."
                    )
                    return@launch
                }

                // Comprobar que el ID no esté registrado.
                if (repositorio.existeUsuario(idLimpio)) {
                    _uiState.value = _uiState.value.copy(
                        mensajeError = "Ya existe un usuario con ese ID."
                    )
                    return@launch
                }

                // Validar supervisor cuando se proporciona.
                if (
                    rolLimpio == "OPERARIO" &&
                    supervisorLimpio != null
                ) {
                    val supervisor = repositorio.obtenerSupervisorActivo(
                        supervisorLimpio
                    )

                    if (supervisor == null) {
                        _uiState.value = _uiState.value.copy(
                            mensajeError = "El supervisor seleccionado no existe o está inactivo."
                        )
                        return@launch
                    }
                }

                // Generar hash seguro de la contraseña.
                val contrasenaHash = GestorContrasenas.generarHash(
                    contrasena
                )

                // Construir nuevo usuario.
                val nuevoUsuario = Usuario(
                    id = idLimpio,
                    contrasenaHash = contrasenaHash,
                    nombreCompleto = nombreLimpio,
                    cargo = cargoLimpio,
                    rol = rolLimpio,
                    activo = true,
                    supervisorId = if (rolLimpio == "OPERARIO") {
                        supervisorLimpio
                    } else {
                        null
                    },
                    faenaId = null
                )

                repositorio.crearUsuario(nuevoUsuario)

                _uiState.value = _uiState.value.copy(
                    mensaje = "Usuario creado correctamente.",
                    mensajeError = null
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    mensajeError = "No fue posible crear el usuario."
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    creandoUsuario = false
                )
                bloqueoCreacion.unlock()
            }
        }
    }

    // --------------------------------------------------
    // ACTUALIZAR ESTADO DEL USUARIO
    // --------------------------------------------------

    fun actualizarEstadoUsuario(
        id: String,
        activo: Boolean
    ) {
        viewModelScope.launch {
            if (!bloqueoActualizacion.tryLock()) return@launch

            try {
                _uiState.value = _uiState.value.copy(
                    usuarioActualizandoId = id,
                    mensaje = null,
                    mensajeError = null
                )

                repositorio.actualizarEstadoUsuario(id, activo)

                _uiState.value = _uiState.value.copy(
                    mensaje = if (activo) {
                        "Usuario activado correctamente."
                    } else {
                        "Usuario desactivado correctamente."
                    }
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    mensajeError = "No fue posible actualizar el estado del usuario."
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    usuarioActualizandoId = null
                )
                bloqueoActualizacion.unlock()
            }
        }
    }

    // --------------------------------------------------
    // LISTAR PERSONAL A CARGO
    // --------------------------------------------------

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
                    mensajeErrorPersonal = "No fue posible cargar el personal a cargo."
                )
            }
        }
    }

    // --------------------------------------------------
    // ASIGNAR FAENA A USUARIO
    // --------------------------------------------------

    fun asignarFaena(
        usuarioId: String,
        faenaId: Long?
    ) {
        viewModelScope.launch {
            try {
                repositorio.asignarFaena(
                    usuarioId = usuarioId,
                    faenaId = faenaId
                )

                _uiState.value = _uiState.value.copy(
                    mensaje = "Faena asignada correctamente.",
                    mensajeError = null
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    mensajeError = "No fue posible asignar la faena."
                )
            }
        }
    }

    // --------------------------------------------------
    // ACTUALIZAR USUARIO
    // --------------------------------------------------

    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                repositorio.actualizarUsuario(usuario)

                _uiState.value = _uiState.value.copy(
                    mensaje = "Usuario actualizado correctamente.",
                    mensajeError = null
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    mensajeError = "No fue posible actualizar el usuario."
                )
            }
        }
    }

    // --------------------------------------------------
    // ELIMINAR USUARIO
    // --------------------------------------------------

    fun eliminarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                repositorio.eliminarUsuario(usuario)

                _uiState.value = _uiState.value.copy(
                    mensaje = "Usuario eliminado correctamente.",
                    mensajeError = null
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    mensajeError = "No fue posible eliminar el usuario."
                )
            }
        }
    }

    // --------------------------------------------------
    // LIMPIAR MENSAJES
    // --------------------------------------------------

    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(
            mensaje = null,
            mensajeError = null
        )
    }
}
