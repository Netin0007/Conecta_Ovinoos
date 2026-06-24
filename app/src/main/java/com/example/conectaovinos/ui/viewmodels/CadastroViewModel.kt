package com.example.conectaovinos.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.data.RebanhoRepository
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CadastroViewModel(private val repository: RebanhoRepository) : ViewModel() {

    // Guarda o estado atual da tela de forma reativa e segura
    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    // Única porta de entrada de comandos da UI
    fun onEvent(event: CadastroEvent) {
        when (event) {
            is CadastroEvent.SetModo -> _uiState.update { it.copy(isAnimalMode = event.isAnimal) }
            is CadastroEvent.AddPhoto -> _uiState.update { it.copy(photoUris = it.photoUris + event.uri) }
            is CadastroEvent.RemovePhoto -> _uiState.update { it.copy(photoUris = it.photoUris.toMutableList().apply { removeAt(event.index) }) }
            is CadastroEvent.OnAddressChanged -> _uiState.update { it.copy(endereco = event.value) }
            is CadastroEvent.OnLocationChanged -> _uiState.update { it.copy(latitude = event.lat, longitude = event.lon) }
            is CadastroEvent.OnProductTypeChanged -> handleProductTypeChange(event.type)
            is CadastroEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
            
            is CadastroEvent.OnEspecieChanged -> _uiState.update { it.copy(especie = event.value) }
            is CadastroEvent.OnBrincoChanged -> _uiState.update { it.copy(brinco = event.value) }
            is CadastroEvent.OnNomeChanged -> _uiState.update { it.copy(nome = event.value) }
            is CadastroEvent.OnSexoChanged -> _uiState.update { it.copy(sexo = event.value) }
            is CadastroEvent.OnRacaChanged -> _uiState.update { it.copy(raca = event.value) }
            is CadastroEvent.OnPesoChanged -> _uiState.update { it.copy(peso = event.value) }
            is CadastroEvent.OnVacinadoChanged -> _uiState.update { it.copy(isVacinado = event.value) }
            
            is CadastroEvent.OnCodigoLoteChanged -> _uiState.update { it.copy(codigoLote = event.value) }
            is CadastroEvent.OnQuantidadeChanged -> _uiState.update { it.copy(quantidade = event.value) }
            is CadastroEvent.OnPrecoChanged -> _uiState.update { it.copy(preco = event.value) }
            
            is CadastroEvent.LoadProduto -> carregarProduto(event.id)
            is CadastroEvent.Submit -> validarESalvar()
        }
    }

    private fun carregarProduto(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val produto = repository.getProdutoById(id)
            if (produto != null) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isEditMode = true,
                        existingId = id,
                        isAnimalMode = produto is AnimalLote,
                        photoUris = produto.imageUrls.map { Uri.parse(it) },
                        endereco = produto.endereco,
                        latitude = produto.latitude,
                        longitude = produto.longitude,
                        preco = produto.custoTotal.toString()
                    )
                }
                
                if (produto is AnimalLote) {
                    _uiState.update { it.copy(
                        especie = produto.especie,
                        brinco = produto.brinco,
                        nome = produto.nome,
                        sexo = produto.sexo,
                        raca = produto.raca,
                        peso = produto.peso.toString(),
                        isVacinado = produto.vacinado
                    )}
                } else if (produto is ProdutoProcessado) {
                    _uiState.update { it.copy(
                        codigoLote = produto.codigoLote,
                        quantidade = produto.quantidade.toString(),
                        productType = produto.tipoProduto,
                        suggestedUnit = produto.unidadeMedida
                    )}
                }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Produto não encontrado") }
            }
        }
    }

    // REGRA DE NEGÓCIO: Alteração automática de unidade baseada no tipo de derivado
    private fun handleProductTypeChange(type: String) {
        val novaUnidade = when (type) {
            "Leite" -> "Litro"
            "Queijo", "Carne", "Lã" -> "KG"
            "Manta de Carneiro" -> "UND"
            else -> "UND"
        }
        _uiState.update { it.copy(suggestedUnit = novaUnidade, productType = type) }
    }

    private fun validarESalvar() {
        val state = _uiState.value

        // Validação 1: Pelo menos uma foto
        if (state.photoUris.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Adicione pelo menos uma foto.") }
            // return 
        }

        // Se passou, inicia o loading
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val currentId = state.existingId ?: UUID.randomUUID().toString()
                
                if (state.isAnimalMode) {
                    val animal = AnimalLote(
                        id = currentId,
                        especie = state.especie,
                        quantidade = 1,
                        nome = state.nome,
                        brinco = state.brinco,
                        raca = state.raca,
                        sexo = state.sexo,
                        peso = state.peso.toDoubleOrNull() ?: 0.0,
                        vacinado = state.isVacinado,
                        custoTotal = state.preco.toDoubleOrNull() ?: 0.0,
                        imageUrls = state.photoUris.map { it.toString() },
                        latitude = state.latitude,
                        longitude = state.longitude,
                        endereco = state.endereco
                    )
                    repository.addAnimalLote(animal)
                } else {
                    val derivado = ProdutoProcessado(
                        id = currentId,
                        tipoProduto = state.productType,
                        unidadeMedida = state.suggestedUnit,
                        quantidade = state.quantidade.toDoubleOrNull() ?: 0.0,
                        codigoLote = state.codigoLote,
                        custoTotal = state.preco.toDoubleOrNull() ?: 0.0,
                        imageUrls = state.photoUris.map { it.toString() },
                        latitude = state.latitude,
                        longitude = state.longitude,
                        endereco = state.endereco
                    )
                    repository.addProdutoProcessado(derivado)
                }

                // Sucesso! Tira o loading e avisa a UI para fechar a tela
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao salvar: ${e.message}") }
            }
        }
    }

    class Factory(private val repository: RebanhoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CadastroViewModel(repository) as T
        }
    }
}
