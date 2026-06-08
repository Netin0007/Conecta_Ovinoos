package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.data.RebanhoRepository
import com.example.conectaovinos.models.Produto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel responsável por prover os dados para a tela de Detalhes do Produto.
 * @author Equipe ConectaFazenda
 * @description Mantido o nome da classe original (AnimalDetailsViewModel) para compatibilidade de rotas,
 * mas agora a arquitetura opera de forma genérica com a interface Produto (Lotes e Derivados).
 */
@HiltViewModel
class AnimalDetailsViewModel @Inject constructor(
    private val repository: RebanhoRepository
) : ViewModel() {

    /**
     * Estado reativo (StateFlow) que contém o inventário completo da fazenda.
     * A tela de detalhes observa este fluxo de dados em tempo real.
     */
    val produtos: StateFlow<List<Produto>> = repository.produtos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /**
     * Função utilitária para buscar um registro específico (Lote Vivo ou Processado)
     * no cache de memória atual de forma síncrona.
     * * @param id O identificador único do registro no banco de dados (UUID).
     * @return O Produto encontrado ou nulo se não existir.
     */
    fun getProduto(id: String?): Produto? {
        return produtos.value.find { it.id == id }
    }
}