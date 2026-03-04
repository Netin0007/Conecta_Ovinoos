package com.example.conectaovinos.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.conectaovinos.database.entities.ManejoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ManejoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addManejo(manejo: ManejoEntity)

    @Query("SELECT * FROM manejoTable WHERE animalId = :animalId ORDER BY id DESC")
    fun getManejosDoAnimal(animalId: String): Flow<List<ManejoEntity>>
}