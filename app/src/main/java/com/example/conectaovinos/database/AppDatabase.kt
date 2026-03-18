package com.example.conectaovinos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.conectaovinos.database.dao.AnimalDao
import com.example.conectaovinos.database.dao.AnuncioDao
import com.example.conectaovinos.database.dao.ManejoDao
import com.example.conectaovinos.database.dao.ProdutoDao
import com.example.conectaovinos.database.dao.TransacaoDao
import com.example.conectaovinos.database.dao.UsuarioDao
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.entities.AnuncioEntity
import com.example.conectaovinos.database.entities.ManejoEntity
import com.example.conectaovinos.database.entities.ProdutosEntity
import com.example.conectaovinos.database.entities.TransacaoEntity
import com.example.conectaovinos.database.entities.UsuarioEntity

@Database(
    entities = [
        AnimalEntity::class,
        ManejoEntity::class,
        ProdutosEntity::class,
        UsuarioEntity::class,
        TransacaoEntity::class,
        AnuncioEntity::class
    ],
    version = 4,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun animalDao(): AnimalDao
    abstract fun produtosDao(): ProdutoDao
    abstract fun manejoDao(): ManejoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun transacaoDao(): TransacaoDao
    abstract fun anuncioDao(): AnuncioDao


    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ConectaOvinoDB"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}