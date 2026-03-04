package com.example.conectaovinos.database.repository

import com.example.conectaovinos.database.dao.ProdutoDao
import com.example.conectaovinos.database.entities.ProdutosEntity
import kotlinx.coroutines.flow.firstOrNull

class ProdutoRepository(private val dao: ProdutoDao) {

    val produtos = dao.getAll()

    suspend fun getById(id: Int) = dao.getById(id)

    suspend fun insertProduto(produto: ProdutosEntity) {
        dao.addProduto(produto)
    }

    suspend fun updateProduto(produto: ProdutosEntity) {
        dao.updateProduto(produto)
    }

    suspend fun updateQuantidade(id: Int, qtd: Int) {
        dao.updateQuantidade(id, qtd)
    }

    suspend fun updatePreco(id: Int, preco: Double) {
        dao.updatePreco(id, preco)
    }

    suspend fun deleteProduto(produto: ProdutosEntity) {
        dao.deleteProduto(produto)
    }
}