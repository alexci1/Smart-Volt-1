
package com.example.smartvolt.datos.repositorio

import com.example.smartvolt.datos.local.dao.UsuarioDAO
import com.example.smartvolt.modelo.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepositorio(
    private val usuarioDAO: UsuarioDAO
) {

    // Listar todos los usuarios.
    fun listarUsuarios(): Flow<List<Usuario>> {
        return usuarioDAO.listarUsuarios()
    }

    // Actualizar el estado de un usuario.
    suspend fun actualizarEstadoUsuario(
        id: String,
        activo: Boolean
    ) {
        usuarioDAO.actualizarEstadoUsuario(
            id = id,
            activo = activo
        )
    }

    // Obtener el personal a cargo de un supervisor.
    fun listarPersonalPorSupervisor(
        supervisorId: String
    ): Flow<List<Usuario>> {
        return usuarioDAO.listarPersonalPorSupervisor(
            supervisorId = supervisorId
        )
    }
}
