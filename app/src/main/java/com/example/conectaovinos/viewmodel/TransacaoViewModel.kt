package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.entities.TransacaoEntity
import com.example.conectaovinos.database.repository.TransacaoRepository
import kotlinx.coroutines.launch

class TransacaoViewModel(private val repository: TransacaoRepository) : ViewModel() {

    val transacoes = repository.getAllTransacoes()
    fun addTransacao(transacao: TransacaoEntity) {
        viewModelScope.launch {
            repository.insert(transacao)
        }
    }
}
