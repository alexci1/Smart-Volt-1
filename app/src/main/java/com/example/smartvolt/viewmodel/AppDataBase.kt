package com.example.smartvolt.viewmodel

import  com.example.smartvolt.modelo.Usuario
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartvolt.modelo.UsuarioDAO
import android.content.Context
import androidx.room.Room


@Database(entities = [Usuario::class], version = 1, exportSchema = false)
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