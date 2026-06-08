package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.data.AnuncioRepository
import com.example.conectaovinos.data.TransacaoRepository
import com.example.conectaovinos.models.Anuncio
import com.example.conectaovinos.models.TipoTransacao
import com.example.conectaovinos.models.Transacao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TransacaoRepository,
    private val anuncioRepository: AnuncioRepository
) : ViewModel() {

    val transacoes: StateFlow<List<Transacao>> = repository.transacoes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val todosAnuncios: StateFlow<List<Anuncio>> = anuncioRepository.todosAnuncios
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addTransacao(descricao: String, valor: Double, tipo: TipoTransacao, categoria: String) {
        viewModelScope.launch {
            repository.addTransacao(
                Transacao(
                    id = UUID.randomUUID().toString(),
                    descricao = descricao,
                    valor = valor,
                    tipo = tipo,
                    data = Date(),
                    categoria = categoria
                )
            )
        }
    }

    fun getTotalReceitas(lista: List<Transacao>) =
        lista.filter { it.tipo == TipoTransacao.Receita }.sumOf { it.valor }

    fun getTotalDespesas(lista: List<Transacao>) =
        lista.filter { it.tipo == TipoTransacao.Despesa }.sumOf { it.valor }

    fun getLucroLiquido(lista: List<Transacao>) =
        getTotalReceitas(lista) - getTotalDespesas(lista)

    fun getLucroEsperadoAnuncios(anuncios: List<Anuncio>): Double {
        return anuncios.filter { it.ativo }.sumOf { it.precoVenda - it.custoAnimal }
    }
}