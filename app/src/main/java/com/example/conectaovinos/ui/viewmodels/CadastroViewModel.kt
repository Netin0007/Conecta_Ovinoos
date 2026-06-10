package com.example.conectaovinos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CadastroViewModel : ViewModel() {

    // Guarda o estado atual da tela de forma reativa e segura
    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    // Única porta de entrada de comandos da UI
    fun onEvent(event: CadastroEvent) {
        when (event) {
            is CadastroEvent.SetModo -> _uiState.update { it.copy(isAnimalMode = event.isAnimal) }
            is CadastroEvent.SetPhoto -> _uiState.update { it.copy(photoUri = event.uri) }
            is CadastroEvent.OnProductTypeChanged -> handleProductTypeChange(event.type)
            is CadastroEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
            is CadastroEvent.Submit -> validarESalvar()
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
        _uiState.update { it.copy(suggestedUnit = novaUnidade) }
    }

    private fun validarESalvar() {
        val state = _uiState.value

        // Validação 1: Foto Obrigatória
        if (state.photoUri == null) {
            _uiState.update { it.copy(errorMessage = "A foto é obrigatória para o cadastro. Por favor, adicione uma imagem.") }
            return
        }

        // Se passou, inicia o loading
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                // Aqui no futuro você vai chamar o Repositório do Firebase.
                // Exemplo: repository.salvarNoFirestore(animalModel)

                // Por enquanto, simulamos o tempo de salvamento na nuvem (1,5 segundos)
                delay(1500)

                // Sucesso! Tira o loading e avisa a UI para fechar a tela
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao salvar: ${e.message}") }
            }
        }
    }
}