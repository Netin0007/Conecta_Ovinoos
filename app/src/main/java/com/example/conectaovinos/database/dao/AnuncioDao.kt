package com.example.conectaovinos.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.conectaovinos.database.entities.AnuncioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnuncioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anuncio: AnuncioEntity)

    @Query("SELECT * FROM anuncios ORDER BY id DESC")
    fun getAll(): Flow<List<AnuncioEntity>>

    @Query("DELETE FROM anuncios WHERE id = :id")
    suspend fun deleteById(id: Int)
}