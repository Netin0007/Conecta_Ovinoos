package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.data.RebanhoRepository
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoProcessado
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel responsável por gerenciar o estado e os filtros da vitrine (Marketplace).
 * @author Equipe ConectaFazenda
 * @description Integra-se com o repositório unificado para buscar os produtos disponíveis e
 * aplica lógicas de filtro e busca em memória para a listagem na feira virtual.
 */
@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val repository: RebanhoRepository
) : ViewModel() {

    /** Fluxo de dados reativo com a lista completa do inventário. */
    val produtos: StateFlow<List<Produto>> = repository.produtos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /**
     * Aplica filtros de categoria e de texto (busca) sobre a lista de produtos.
     * @param lista A lista completa carregada do banco.
     * @param categoria A aba selecionada (Ex: "Todos", "Animais", "Derivados").
     * @param busca Termo digitado pelo usuário na barra de pesquisa.
     * @return Lista filtrada pronta para a UI.
     */
    fun filtrar(lista: List<Produto>, categoria: String, busca: String): List<Produto> {
        return lista.filter { produto ->
            // Mapeamento atualizado para a nova arquitetura da Fazenda (Lotes e Derivados)
            val categoriaOk = when (categoria) {
                "Animais" -> produto is AnimalLote
                "Derivados" -> produto is ProdutoProcessado
                else -> true
            }

            // Utilizando a nova propriedade 'nomeAmigavel' do chassi Produto
            val buscaOk = produto.nomeAmigavel.contains(busca, ignoreCase = true)

            categoriaOk && buscaOk
        }
    }

    /**
     * Busca de produto específico por ID.
     */
    fun getProdutoById(id: String): Produto? = produtos.value.find { it.id == id }
}