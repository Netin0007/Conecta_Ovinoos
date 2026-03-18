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
    fun getAll(): kotlinx.coroutines.flow.Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animals WHERE id = :animalId LIMIT 1")
    fun getById(animalId: Int): Flow<AnimalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(animal: AnimalEntity)

    @Update
    suspend fun updateAnimal(animal: AnimalEntity)

    @Query("UPDATE animals SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Delete
    suspend fun deleteAnimal(animal: AnimalEntity)

    @Query("DELETE FROM animals")
    suspend fun deleteAllAnimals()
}