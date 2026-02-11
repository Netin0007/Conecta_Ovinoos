package com.example.conectaovinos.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.conectaovinos.database.entities.ProdutoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduto(produto: ProdutoEntity)

    @Query("SELECT * FROM produtos ORDER BY id ASC")
    fun getAllProdutos(): Flow<List<ProdutoEntity>>

    @Update
    suspend fun updateProduto(produto: ProdutoEntity)

    @Query("UPDATE produtos SET quantidade = :qtd WHERE id = :id")
    suspend fun updateQuantidade(id: Int, qtd: Int)

    @Query("UPDATE produtos SET preco = :preco WHERE id = :id")
    suspend fun updatePreco(id: Int, preco: Double)

    @Delete
    suspend fun deleteProduto(produto: ProdutoEntity)

}