package com.example.personaltasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.personaltasks.model.Task

// Instanciação do banco de dados
@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // chamada do DAO
    abstract fun taskDAO(): TaskDAO

    // utilizando padrão singleton para realizar instância do banco de dados
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Metodo que inicializa a instância do banco de dados
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "personal_tasks_db"
                            ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
