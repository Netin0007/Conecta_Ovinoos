package com.example.conectaovinos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.conectaovinos.database.entities.TransacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransacaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(t: TransacaoEntity)

    @Query("SELECT * FROM transacoes ORDER BY id DESC")
    fun getAll(): Flow<List<TransacaoEntity>>
}