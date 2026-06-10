package com.example.conectaovinos.ui.viewmodels

import android.net.Uri

//Tudo o que o produtor pode FAZER na tela
sealed class CadastroEvent {
    data class SetModo(val isAnimal: Boolean) : CadastroEvent()
    data class SetPhoto(val uri: Uri?) : CadastroEvent()
    data class OnProductTypeChanged(val type: String) : CadastroEvent()
    object Submit : CadastroEvent()
    object ClearError : CadastroEvent()
}

//Tudo o que a tela precisa MOSTRAR
data class CadastroUiState(
    val isLoading: Boolean = false,
    val isAnimalMode: Boolean = true,
    val photoUri: Uri? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,

    // Listas padronizadas para os Dropdowns (menus de seleção)
    val especies: List<String> = listOf("Ovino", "Bovino", "Caprino", "Suíno", "Equino", "Outro"),
    val tiposDerivado: List<String> = listOf("Leite", "Queijo", "Carne", "Manta de Carneiro", "Lã", "Pele", "Iogurte", "Manteiga", "Outro"),
    val unidadesMedida: List<String> = listOf("KG", "G", "Litro", "mL", "UND"),

    // Unidade inteligente que vai mudar sozinha (Ex: Escolheu Leite -> vira Litro)
    val suggestedUnit: String = "UND"
)