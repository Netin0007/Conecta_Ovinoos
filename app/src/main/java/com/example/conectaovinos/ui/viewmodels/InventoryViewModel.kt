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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed interface InventoryUiState {
    object Loading : InventoryUiState
    data class Success(val produtos: List<Produto>) : InventoryUiState
    data class Error(val message: String) : InventoryUiState
}

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: RebanhoRepository
) : ViewModel() {

    val uiState: StateFlow<InventoryUiState> = repository.produtos
        .map { InventoryUiState.Success(it) as InventoryUiState }
        .catch { emit(InventoryUiState.Error("Falha de conexão: ${it.message}")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InventoryUiState.Loading
        )

    val produtos: StateFlow<List<Produto>> = repository.produtos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // --- NOVA FUNÇÃO: ADICIONA LOTE DE QUALQUER ESPÉCIE ---
    fun addAnimalLote(especie: String, quantidade: Int, custoTotal: Double) {
        viewModelScope.launch {
            repository.addAnimalLote(
                AnimalLote(
                    id = UUID.randomUUID().toString(),
                    especie = especie,
                    quantidade = quantidade,
                    custoTotal = custoTotal
                )
            )
        }
    }

    fun updateAnimalLote(loteAtualizado: AnimalLote) {
        viewModelScope.launch { repository.updateAnimalLote(loteAtualizado) }
    }

    // --- NOVA FUNÇÃO: ADICIONA KG DA CARNE, QUEIJO, ETC ---
    fun addProdutoProcessado(tipoProduto: String, unidadeMedida: String, quantidade: Double, custoTotal: Double) {
        viewModelScope.launch {
            repository.addProdutoProcessado(
                ProdutoProcessado(
                    id = UUID.randomUUID().toString(),
                    tipoProduto = tipoProduto,
                    unidadeMedida = unidadeMedida,
                    quantidade = quantidade,
                    custoTotal = custoTotal
                )
            )
        }
    }

    fun removeProduto(id: String) {
        viewModelScope.launch { repository.removeProduto(id) }
    }
}