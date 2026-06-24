package com.example.conectaovinos.ui.viewmodels

import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado

//O que o produtor faz na tela de Estoque
sealed class InventoryEvent {
    data class SelectTab(val tabIndex: Int) : InventoryEvent()
    data class DeleteItem(val id: String) : InventoryEvent()
    object RefreshData : InventoryEvent()
}

//O que a tela renderiza
data class InventoryUiState(
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0,
    val animais: List<AnimalLote> = emptyList(),
    val derivados: List<ProdutoProcessado> = emptyList(),
    val valorTotalEstoque: Double = 0.0,
    val errorMessage: String? = null
)
