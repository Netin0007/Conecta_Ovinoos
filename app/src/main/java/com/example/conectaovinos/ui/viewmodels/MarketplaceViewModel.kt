package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.data.RebanhoRepository
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MarketplaceViewModel(private val repository: RebanhoRepository) : ViewModel() {

    val produtos: StateFlow<List<Produto>> = repository.produtos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun filtrar(lista: List<Produto>, categoria: String, busca: String): List<Produto> {
        return lista.filter { produto ->
            val categoriaOk = when (categoria) {
                "Animais" -> produto is Animal
                "Derivados" -> produto !is Animal
                else -> true
            }
            val buscaOk = produto.nome.contains(busca, ignoreCase = true)
            categoriaOk && buscaOk
        }
    }

    fun getProdutoById(id: String): Produto? = produtos.value.find { it.id == id }

    class Factory(private val repository: RebanhoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MarketplaceViewModel(repository) as T
        }
    }
}