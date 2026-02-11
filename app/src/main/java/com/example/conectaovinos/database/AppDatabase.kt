package com.example.conectaovinos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.conectaovinos.database.dao.AnimalDao
import com.example.conectaovinos.database.dao.ManejoDao
import com.example.conectaovinos.database.dao.ProdutoDao
import com.example.conectaovinos.database.dao.TransacaoDao
import com.example.conectaovinos.database.dao.UsuarioDao
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.entities.ManejoEntity
import com.example.conectaovinos.database.entities.ProdutoEntity
import com.example.conectaovinos.database.entities.TransacaoEntity
import com.example.conectaovinos.database.entities.UsuarioEntity


// CRIAÇÃO BANCO DE DADOS
@Database(entities = [
    AnimalEntity::class,
    ManejoEntity::class,
    ProdutoEntity::class,
    UsuarioEntity::class,
    TransacaoEntity::class
                     ], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun AnimalDao(): AnimalDao
    abstract fun ProdutoDao(): ProdutoDao
    abstract fun ManejoDao(): ManejoDao
    abstract fun UsuarioDao(): UsuarioDao
    abstract fun TransacaoDao(): TransacaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase?= null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this){
                val instance  = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ConectaOvinoDB")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}