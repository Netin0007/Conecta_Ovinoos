package com.example.conectaovinos.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.conectaovinos.database.entities.AnimalEntity
import kotlinx.coroutines.flow.Flow


// CRIAR ACESSO AO BANCO


@Dao
interface AnimalDao{
    @Query("SELECT * FROM animals")
    fun getall(): Flow<List<AnimalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(animal : AnimalEntity)

    @Query("DELETE FROM animals")
    suspend fun deleteAll()
}