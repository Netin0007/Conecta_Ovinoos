package com.example.conectaovinos.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.conectaovinos.database.entities.TransacaoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TransacaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransacao(transacao: TransacaoEntity)

    @Query("SELECT * FROM transacoes ORDER BY data DESC")
    fun getAllTransacoes(): Flow<List<TransacaoEntity>>

    @Query("SELECT SUM(valor) FROM transacoes WHERE tipo = 'VENDA'")
    suspend fun getTotalVendas(): Double?

    @Query("SELECT SUM(valor) FROM transacoes WHERE tipo = 'COMPRA'")
    suspend fun getTotalCompras(): Double?

    @Delete
    suspend fun deleteTransacao(transacao: TransacaoEntity)
}