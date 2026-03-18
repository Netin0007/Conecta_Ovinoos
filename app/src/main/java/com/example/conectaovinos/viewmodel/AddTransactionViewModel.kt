package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.dao.TransacaoDao
import com.example.conectaovinos.database.entities.TransacaoEntity
import com.example.conectaovinos.database.enums.tipoTransacao
import kotlinx.coroutines.launch

class AddTransactionViewModel(private val transacaoDao: TransacaoDao) : ViewModel() {

    fun saveTransaction(
        tipo: tipoTransacao,
        descricao: String,
        valor: Double,
        categoria: String,
        data: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val transacao = TransacaoEntity(
                tipo = tipo,
                descricao = descricao,
                valor = valor,
                categoria = categoria,
                data = data
            )
            transacaoDao.addTransaction(transacao)
            onSuccess()
        }
    }
}

class AddTransactionViewModelFactory(private val transacaoDao: TransacaoDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddTransactionViewModel(transacaoDao) as T
    }
}