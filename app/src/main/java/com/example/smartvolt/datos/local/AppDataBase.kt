
package com.example.smartvolt.datos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartvolt.datos.local.dao.AsistenciaDAO
import com.example.smartvolt.datos.local.dao.UsuarioDAO
import com.example.smartvolt.modelo.Asistencia
import com.example.smartvolt.modelo.Usuario

@Database(
    entities = [
        Usuario::class,
        Asistencia::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDAO(): UsuarioDAO

    abstract fun asistenciaDAO(): AsistenciaDAO

    companion object {

        @Volatile
        private var INSTANCIA: AppDataBase? = null

        fun obtenerBaseDatos(context: Context): AppDataBase {
            return INSTANCIA ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "asistencia_faena_db"
                )
                    .fallbackToDestructiveMigration(
                        dropAllTables = true
                    )
                    .build()

                INSTANCIA = instancia
                instancia
            }
        }
    }
}
