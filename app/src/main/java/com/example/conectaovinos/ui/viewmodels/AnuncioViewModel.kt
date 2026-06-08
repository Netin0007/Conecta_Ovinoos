package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.data.AnuncioRepository
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoProcessado
import com.example.conectaovinos.models.Anuncio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel responsável pela lógica de negócios da Feira Livre (Marketplace).
 * @author Equipe ConectaFazenda
 * @description Gerencia a publicação, pausa e exclusão de anúncios.
 * Utiliza o padrão de adaptador (Adapter Pattern) para converter Produtos Genéricos
 * no formato de Anúncio compatível com o banco de dados existente.
 */
@HiltViewModel
class AnuncioViewModel @Inject constructor(
    private val repository: AnuncioRepository
) : ViewModel() {

    /** Cache reativo de anúncios ativos disponíveis para venda */
    val anunciosAtivos: StateFlow<List<Anuncio>> = repository.anunciosAtivos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Histórico completo de anúncios para o painel do produtor */
    val todosAnuncios: StateFlow<List<Anuncio>> = repository.todosAnuncios
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /**
     * Publica um novo anúncio no marketplace.
     * @param produto O Lote Vivo ou Produto Processado que será vendido.
     * @param precoVenda Valor final estipulado pelo produtor.
     * @param descricao Texto de apoio para marketing do produto.
     */
    fun publicarAnuncio(produto: Produto, precoVenda: Double, descricao: String) {
        viewModelScope.launch {
            // Extrai o detalhe dinâmico (Espécie se for bicho, Tipo se for queijo/carne)
            val detalheProduto = when (produto) {
                is AnimalLote -> produto.especie
                is ProdutoProcessado -> produto.tipoProduto
            }

            repository.publicar(
                Anuncio(
                    id = UUID.randomUUID().toString(),
                    animalId = produto.id,
                    nomeAnimal = produto.nomeAmigavel,
                    racaAnimal = detalheProduto,
                    custoAnimal = produto.custoTotal,
                    precoVenda = precoVenda,
                    descricao = descricao,
                    ativo = true,
                    dataCriacao = Date()
                )
            )
        }
    }

    fun pausarAnuncio(id: String) {
        viewModelScope.launch { repository.pausar(id) }
    }

    fun reativarAnuncio(id: String) {
        viewModelScope.launch { repository.reativar(id) }
    }

    fun deletarAnuncio(id: String) {
        viewModelScope.launch { repository.deletar(id) }
    }

    fun buscarAnuncioPorId(id: String): Anuncio? =
        anunciosAtivos.value.find { it.id == id }
            ?: todosAnuncios.value.find { it.id == id }

    fun isProdutoJaAnunciado(produtoId: String): Boolean =
        anunciosAtivos.value.any { it.animalId == produtoId }
}