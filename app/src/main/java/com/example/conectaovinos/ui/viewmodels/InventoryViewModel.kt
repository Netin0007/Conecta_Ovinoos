package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.data.RebanhoRepository
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: RebanhoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        observarDadosReais()
    }

    private fun observarDadosReais() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.produtos.collectLatest { listaProdutos ->
                val animais = listaProdutos.filterIsInstance<AnimalLote>()
                val derivados = listaProdutos.filterIsInstance<ProdutoProcessado>()
                
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        animais = animais,
                        derivados = derivados,
                        valorTotalEstoque = listaProdutos.sumOf { it.custoTotal }
                    )
                }
            }
        }
    }

    fun onEvent(event: InventoryEvent) {
        when (event) {
            is InventoryEvent.SelectTab -> _uiState.update { it.copy(selectedTabIndex = event.tabIndex) }
            is InventoryEvent.RefreshData -> {} 
            is InventoryEvent.DeleteItem -> deletarItem(event.id)
        }
    }

    private fun deletarItem(id: String) {
        viewModelScope.launch {
            repository.removeProduto(id)
        }
    }

    class Factory(private val repository: RebanhoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(repository) as T
        }
    }
}
