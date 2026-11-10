
package com.example.smartvolt.datos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smartvolt.datos.local.dao.AsistenciaDAO
import com.example.smartvolt.datos.local.dao.UsuarioDAO
import com.example.smartvolt.modelo.Asistencia
import com.example.smartvolt.modelo.Usuario

@Database(
    entities = [
        Usuario::class,
        Asistencia::class
    ],
    version = 4,
    exportSchema = true
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDAO(): UsuarioDAO

    abstract fun asistenciaDAO(): AsistenciaDAO

    companion object {

        @Volatile
        private var INSTANCIA: AppDataBase? = null

        private val MIGRACION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE usuarios ADD COLUMN supervisorId TEXT DEFAULT NULL"
                )
            }
        }

        fun obtenerBaseDatos(context: Context): AppDataBase {
            return INSTANCIA ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "asistencia_faena_db"
                )
                    .addMigrations(MIGRACION_3_4)
                    .build()

                INSTANCIA = instancia
                instancia
            }
        }
    }
}
