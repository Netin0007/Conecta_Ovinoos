package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.models.AnimalModel
import com.example.conectaovinos.models.DerivadoModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class InventoryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        // Quando o ViewModel nasce, ele busca os dados (aqui estamos simulando o Firebase)
        carregarDadosMockParaTesteUI()
    }

    fun onEvent(event: InventoryEvent) {
        when (event) {
            is InventoryEvent.SelectTab -> _uiState.update { it.copy(selectedTabIndex = event.tabIndex) }
            is InventoryEvent.RefreshData -> carregarDadosMockParaTesteUI()
            is InventoryEvent.DeleteItem -> deletarItem(event.id, event.isAnimal)
        }
    }

    private fun calcularValorTotal(animais: List<AnimalModel>, derivados: List<DerivadoModel>): Double {
        val somaAnimais = animais.sumOf { it.salePrice } // Ou custo, dependendo da sua regra
        val somaDerivados = derivados.sumOf { it.salePrice }
        return somaAnimais + somaDerivados
    }

    private fun deletarItem(id: String, isAnimal: Boolean) {
        // No futuro: repository.deleteFromFirebase(id)
        _uiState.update { state ->
            if (isAnimal) {
                val novaLista = state.animais.filter { it.id != id }
                state.copy(animais = novaLista, valorTotalEstoque = calcularValorTotal(novaLista, state.derivados))
            } else {
                val novaLista = state.derivados.filter { it.id != id }
                state.copy(derivados = novaLista, valorTotalEstoque = calcularValorTotal(state.animais, novaLista))
            }
        }
    }

    // TODO: Substituir essa função pela busca real no Firebase Firestore
    private fun carregarDadosMockParaTesteUI() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(800) // Finge que tá baixando da internet

            val mockAnimais = listOf(
                AnimalModel(id = "1", name = "Estrela", earTag = "BR-1029", animalType = "Ovino", breed = "Santa Inês", sex = "Fêmea", weight = 45.0, salePrice = 850.0),
                AnimalModel(id = "2", earTag = "BR-3341", animalType = "Bovino", breed = "Nelore", sex = "Macho", weight = 320.0, salePrice = 3500.0)
            )
            val mockDerivados = listOf(
                DerivadoModel(id = "3", batchCode = "LT-001", productType = "Queijo", unit = "KG", quantity = 15.0, salePrice = 750.0),
                DerivadoModel(id = "4", batchCode = "LT-002", productType = "Manta de Carneiro", unit = "UND", quantity = 2.0, salePrice = 300.0)
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    animais = mockAnimais,
                    derivados = mockDerivados,
                    valorTotalEstoque = calcularValorTotal(mockAnimais, mockDerivados)
                )
            }
        }
    }
}