package com.example.conectaovinos.data.local.dao

import androidx.room.*
import com.example.conectaovinos.data.local.entity.TransacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransacaoDao {

    @Query("SELECT * FROM transacoes ORDER BY dataMs DESC")
    fun observarTransacoes(): Flow<List<TransacaoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirTransacao(transacao: TransacaoEntity)

    @Query("SELECT * FROM transacoes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: String): TransacaoEntity?

    @Delete
    suspend fun deletarTransacao(transacao: TransacaoEntity)
}