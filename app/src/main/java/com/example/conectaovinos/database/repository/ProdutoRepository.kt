package com.example.conectaovinos.database.repository

import com.example.conectaovinos.database.dao.ProdutoDao
import com.example.conectaovinos.database.entities.ProdutoEntity

class ProdutoRepository(private val dao: ProdutoDao) {

    val produtos = dao.getAllProdutos()

    suspend fun insertProduto(produto: ProdutoEntity) {
        dao.addProduto(produto)
    }

    suspend fun updateProduto(produto: ProdutoEntity) {
        dao.updateProduto(produto)
    }

    suspend fun updateQuantidade(id: Int, qtd: Int) {
        dao.updateQuantidade(id, qtd)
    }

    suspend fun updatePreco(id: Int, preco: Double) {
        dao.updatePreco(id, preco)
    }

    suspend fun deleteProduto(produto: ProdutoEntity) {
        dao.deleteProduto(produto)
    }
}