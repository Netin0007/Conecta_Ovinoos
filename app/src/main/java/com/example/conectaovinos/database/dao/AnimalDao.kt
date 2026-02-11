package com.example.conectaovinos.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.conectaovinos.database.entities.AnimalEntity
import kotlinx.coroutines.flow.Flow


// CRIAR ACESSO AO BANCO


@Dao
interface AnimalDao {

    @Query("SELECT * FROM animals")
    fun getAll(): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animals WHERE id = :id LIMIT 1")
    suspend fun getAnimalById(id: Int?): AnimalEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(animal: AnimalEntity)

    @Update
    suspend fun updateAnimal(animal: AnimalEntity)

    @Delete
    suspend fun deleteAnimal(animal: AnimalEntity)

    @Query("DELETE FROM animals")
    suspend fun deleteAllAnimals()
}