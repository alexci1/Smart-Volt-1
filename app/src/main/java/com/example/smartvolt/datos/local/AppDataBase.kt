package com.example.smartvolt.datos.local

import  com.example.smartvolt.modelo.Usuario
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartvolt.datos.local.dao.UsuarioDAO
import android.content.Context
import androidx.room.Room


@Database(entities = [Usuario::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDAO(): UsuarioDAO

    companion object{
        @Volatile
        private var INSTANCIA: AppDataBase? = null

        fun obtenerBaseDatos(context: Context) : AppDataBase{
            return INSTANCIA ?: synchronized(this){
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "asistencia_faena_db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}