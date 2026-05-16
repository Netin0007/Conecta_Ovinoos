package com.example.conectaovinos.data

import com.example.conectaovinos.data.local.dao.TransacaoDao
import com.example.conectaovinos.data.local.mapper.toEntity
import com.example.conectaovinos.data.local.mapper.toModel
import com.example.conectaovinos.models.Transacao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransacaoRepository(private val dao: TransacaoDao) {

    val transacoes: Flow<List<Transacao>> = dao
        .observarTransacoes()
        .map { lista -> lista.map { it.toModel() } }

    suspend fun addTransacao(transacao: Transacao) {
        dao.inserirTransacao(transacao.toEntity())
    }

    suspend fun removeTransacao(id: String) {
        dao.buscarPorId(id)?.let { dao.deletarTransacao(it) }
    }
}