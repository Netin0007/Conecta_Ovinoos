package com.example.conectaovinos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.conectaovinos.database.dao.AnimalDao
import com.example.conectaovinos.database.entities.AnimalEntity
import kotlinx.serialization.Contextual

// CRIAÇÃO BANCO DE DADOS
@Database(entities = [AnimalEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animalDao(): AnimalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase?= null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this){
                val instace = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ConectaOvinoDB").build()
                INSTANCE = instace
                instace
            }
        }
    }
}