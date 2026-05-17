package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.data.RebanhoRepository
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoDerivado
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class InventoryViewModel(private val repository: RebanhoRepository) : ViewModel() {

    // Converte o Flow do Room em StateFlow para a tela observar
    val produtos: StateFlow<List<Produto>> = repository.produtos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addAnimal(nome: String, raca: String, dataNascimento: String, custo: Double) {
        viewModelScope.launch {
            repository.addAnimal(
                Animal(
                    id = UUID.randomUUID().toString(),
                    nome = nome,
                    raca = raca.ifEmpty { "Sem Raça" },
                    dataNascimento = dataNascimento,
                    custo = custo
                )
            )
        }
    }

    fun addProdutoDerivado(nome: String, unidadeDeMedida: String, custo: Double) {
        viewModelScope.launch {
            repository.addProdutoDerivado(
                ProdutoDerivado(
                    id = UUID.randomUUID().toString(),
                    nome = nome,
                    unidadeDeMedida = unidadeDeMedida.ifEmpty { "Unidade" },
                    custo = custo
                )
            )
        }
    }

    fun removeProduto(id: String) {
        viewModelScope.launch { repository.removeProduto(id) }
    }

    fun getProdutoById(id: String): Produto? = produtos.value.find { it.id == id }

    // Factory — permite passar o repository sem precisar de Hilt
    class Factory(private val repository: RebanhoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(repository) as T
        }
    }
}