
package com.example.smartvolt.datos.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smartvolt.modelo.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtenerUsuarioPorId(id: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarUsuario(usuario: Usuario)

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    @Delete
    suspend fun eliminarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios ORDER BY id ASC")
    fun listarUsuarios(): Flow<List<Usuario>>

    @Query("UPDATE usuarios SET activo = :activo WHERE id = :id")
    suspend fun actualizarEstadoUsuario(
        id: String,
        activo: Boolean
    )

    // Obtener operarios asignados a un supervisor
    @Query("""
        SELECT * FROM usuarios
        WHERE supervisorId = :supervisorId
        AND rol = 'OPERARIO'
        ORDER BY nombreCompleto ASC
    """)
    fun listarPersonalPorSupervisor(
        supervisorId: String
    ): Flow<List<Usuario>>
}
