package com.example.conectaovinos.database.repository

import com.example.conectaovinos.database.dao.TransacaoDao
import com.example.conectaovinos.database.entities.TransacaoEntity

class TransacaoRepository(private val dao: TransacaoDao) {
    suspend fun insert(t: TransacaoEntity) = dao.addTransaction(t)
    val all = dao.getAll()
}
