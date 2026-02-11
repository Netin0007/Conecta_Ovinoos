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

    @Query("SELECT * FROM manejos ORDER BY data DESC")
    fun getAllManejos(): Flow<List<ManejoEntity>>

    @Query("SELECT * FROM manejos WHERE animalId = :animalId")
    fun getManejoByAnimals(animalId: Int?): Flow<List<ManejoEntity>>

    @Delete
    suspend fun deleteManejo(manejo: ManejoEntity)
}