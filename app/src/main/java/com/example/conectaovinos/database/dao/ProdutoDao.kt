package com.example.conectaovinos.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.conectaovinos.database.entities.ProdutosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduto(produto: ProdutosEntity)

    @Query("SELECT * FROM produtosTable ORDER BY id ASC")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<ProdutosEntity>>

    @Query("SELECT * FROM produtosTable WHERE id = :id LIMIT 1")
    fun getById(id: Int): kotlinx.coroutines.flow.Flow<ProdutosEntity?>

    @Update
    suspend fun updateProduto(produto: ProdutosEntity)

    @Query("UPDATE produtosTable SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("UPDATE produtosTable SET quantidade = :qtd WHERE id = :id")
    suspend fun updateQuantidade(id: Int, qtd: Int)

    @Query("UPDATE produtosTable SET preco = :preco WHERE id = :id")
    suspend fun updatePreco(id: Int, preco: Double)

    @Delete
    suspend fun deleteProduto(produto: ProdutosEntity)

}