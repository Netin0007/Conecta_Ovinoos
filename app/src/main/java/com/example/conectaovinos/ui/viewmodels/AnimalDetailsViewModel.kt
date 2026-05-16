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

class AnimalDetailsViewModel(private val repository: RebanhoRepository) : ViewModel() {

    val produtos: StateFlow<List<Produto>> = repository.produtos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getAnimal(id: String?): Animal? {
        return produtos.value.find { it.id == id } as? Animal
    }

    class Factory(private val repository: RebanhoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AnimalDetailsViewModel(repository) as T
        }
    }
}