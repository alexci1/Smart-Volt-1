package com.example.smartvolt.datos.repositorio

import com.example.smartvolt.datos.local.dao.AsistenciaDAO
import com.example.smartvolt.modelo.Asistencia
import kotlinx.coroutines.flow.Flow

class AsistenciaRepositorio(
    private val asistenciaDAO: AsistenciaDAO
) {

    // Registrar una marcación.
    suspend fun registrarAsistencia(
        asistencia: Asistencia
    ): Long {
        return asistenciaDAO.registrarAsistencia(asistencia)
    }

    // Consultar el historial de un operario.
    fun listarAsistenciasPorUsuario(
        usuarioId: String
    ): Flow<List<Asistencia>> {
        return asistenciaDAO.listarAsistenciasPorUsuario(usuarioId)
    }

    // Obtener la última marcación.
    suspend fun obtenerUltimaAsistencia(
        usuarioId: String
    ): Asistencia? {
        return asistenciaDAO.obtenerUltimaAsistencia(usuarioId)
    }
}
