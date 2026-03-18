package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.dao.AnimalDao
import com.example.conectaovinos.database.dao.TransacaoDao
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.entities.TransacaoEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val animalDao: AnimalDao,
    private val transacaoDao: TransacaoDao
) : ViewModel() {

    val animais: StateFlow<List<AnimalEntity>> =
        animalDao.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transacoes: StateFlow<List<TransacaoEntity>> =
        transacaoDao.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val financialSummary: StateFlow<FinancialSummary> =
        combine(animais, transacoes) { listAnimais, listTransacoes ->
            val custoAnimais = listAnimais.sumOf { it.preco }
            val totalReceitas = listTransacoes.filter { it.tipo == com.example.conectaovinos.database.enums.tipoTransacao.Receita }.sumOf { it.valor }
            val totalDespesas = listTransacoes.filter { it.tipo == com.example.conectaovinos.database.enums.tipoTransacao.Despesa }.sumOf { it.valor }
            
            FinancialSummary(
                custoRebanho = custoAnimais,
                receitas = totalReceitas,
                despesas = totalDespesas + custoAnimais,
                saldoGeral = totalReceitas - (totalDespesas + custoAnimais)
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FinancialSummary())
}

data class FinancialSummary(
    val custoRebanho: Double = 0.0,
    val receitas: Double = 0.0,
    val despesas: Double = 0.0,
    val saldoGeral: Double = 0.0
)