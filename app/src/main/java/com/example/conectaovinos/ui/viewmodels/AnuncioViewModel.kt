package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.data.AnuncioRepository
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Anuncio
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class AnuncioViewModel(private val repository: AnuncioRepository) : ViewModel() {

    /** Anúncios ativos para o Marketplace */
    val anunciosAtivos: StateFlow<List<Anuncio>> = repository.anunciosAtivos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Todos os anúncios para a tela de gestão do produtor */
    val todosAnuncios: StateFlow<List<Anuncio>> = repository.todosAnuncios
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun publicarAnimal(animal: Animal, precoVenda: Double, descricao: String) {
        viewModelScope.launch {
            repository.publicar(
                Anuncio(
                    id = UUID.randomUUID().toString(),
                    animalId = animal.id,
                    nomeAnimal = animal.nome,
                    racaAnimal = animal.raca,
                    custoAnimal = animal.custo,
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

    fun isAnimalJaAnunciado(animalId: String): Boolean =
        anunciosAtivos.value.any { it.animalId == animalId }

    class Factory(private val repository: AnuncioRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AnuncioViewModel(repository) as T
        }
    }
}