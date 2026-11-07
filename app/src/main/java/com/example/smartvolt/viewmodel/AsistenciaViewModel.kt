package com.example.smartvolt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartvolt.datos.repositorio.AsistenciaRepositorio
import com.example.smartvolt.modelo.Asistencia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AsistenciaUiState(
    val historial: List<Asistencia> = emptyList(),
    val ultimaAsistencia: Asistencia? = null,
    val cargando: Boolean = false,
    val mensaje: String? = null,
    val mensajeError: String? = null
)

class AsistenciaViewModel(
    private val repositorio: AsistenciaRepositorio,
    private val usuarioId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(AsistenciaUiState())
    val uiState: StateFlow<AsistenciaUiState> = _uiState.asStateFlow()

    init {
        observarHistorial()
    }

    // Observar los cambios en el historial del usuario.
    private fun observarHistorial() {
        viewModelScope.launch {
            repositorio.listarAsistenciasPorUsuario(usuarioId)
                .collect { asistencias ->

                    _uiState.value = _uiState.value.copy(
                        historial = asistencias,
                        ultimaAsistencia = asistencias.firstOrNull()
                    )
                }
        }
    }

    // Registrar la siguiente marcación correspondiente.
    fun registrarMarcacion() {

        if (_uiState.value.cargando) return

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                cargando = true,
                mensaje = null,
                mensajeError = null
            )

            try {

                val ultima = repositorio.obtenerUltimaAsistencia(usuarioId)

                val tipo = if (ultima?.tipo == "ENTRADA") {
                    "SALIDA"
                } else {
                    "ENTRADA"
                }

                val asistencia = Asistencia(
                    usuarioId = usuarioId,
                    fechaHora = System.currentTimeMillis(),
                    tipo = tipo
                )

                repositorio.registrarAsistencia(asistencia)

                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    mensaje = if (tipo == "ENTRADA") {
                        "Entrada registrada correctamente."
                    } else {
                        "Salida registrada correctamente."
                    },
                    mensajeError = null
                )

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    mensajeError = "No fue posible registrar la asistencia."
                )
            }
        }
    }

    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(
            mensaje = null,
            mensajeError = null
        )
    }
}
