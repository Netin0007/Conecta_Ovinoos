package com.example.conectaovinos.database.repository

import com.example.conectaovinos.database.dao.TransacaoDao
import com.example.conectaovinos.database.entities.TransacaoEntity

class TransacaoRepository(private val dao: TransacaoDao) {

    val transacoes = dao.getAllTransacoes()

    fun getAllTransacoes() = dao.getAllTransacoes()
    suspend fun insert(transacao: TransacaoEntity) {
        dao.addTransacao(transacao)
    }

    suspend fun getTotalVendas(): Double {
        return dao.getTotalVendas() ?: 0.0
    }

    suspend fun getTotalCompras(): Double {
        return dao.getTotalCompras() ?: 0.0
    }

    suspend fun deleteTransacao(transacao: TransacaoEntity) {
        dao.deleteTransacao(transacao)
    }
}
