package com.example.conectaovinos.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "conectaovinos.db"
            )
                .fallbackToDestructiveMigration() // ✅ permite alterar tabela sem crash
                .build()

            INSTANCE = instance
            instance
        }
    }
}