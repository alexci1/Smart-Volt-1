package com.example.smartvolt.datos.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartvolt.modelo.Asistencia
import kotlinx.coroutines.flow.Flow

@Dao
interface AsistenciaDAO {

    // Registrar una nueva marcación.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrarAsistencia(asistencia: Asistencia): Long

    // Consultar todas las marcaciones de un usuario.
    @Query("""
        SELECT * FROM asistencias
        WHERE usuarioId = :usuarioId
        ORDER BY fechaHora DESC
    """)
    fun listarAsistenciasPorUsuario(
        usuarioId: String
    ): Flow<List<Asistencia>>

    // Obtener la última marcación realizada por un usuario.
    @Query("""
        SELECT * FROM asistencias
        WHERE usuarioId = :usuarioId
        ORDER BY fechaHora DESC
        LIMIT 1
    """)
    suspend fun obtenerUltimaAsistencia(
        usuarioId: String
    ): Asistencia?
}
