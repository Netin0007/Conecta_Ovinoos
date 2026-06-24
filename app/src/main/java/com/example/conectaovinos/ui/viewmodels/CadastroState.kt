package com.example.conectaovinos.ui.viewmodels

import android.net.Uri

//Tudo o que o produtor pode FAZER na tela
sealed class CadastroEvent {
    data class SetModo(val isAnimal: Boolean) : CadastroEvent()
    data class AddPhoto(val uri: Uri) : CadastroEvent()
    data class RemovePhoto(val index: Int) : CadastroEvent()
    data class OnAddressChanged(val value: String) : CadastroEvent()
    data class OnLocationChanged(val lat: Double?, val lon: Double?) : CadastroEvent()
    data class OnProductTypeChanged(val type: String) : CadastroEvent()

    // Novos eventos para capturar inputs
    data class OnEspecieChanged(val value: String) : CadastroEvent()
    data class OnBrincoChanged(val value: String) : CadastroEvent()
    data class OnNomeChanged(val value: String) : CadastroEvent()
    data class OnSexoChanged(val value: String) : CadastroEvent()
    data class OnRacaChanged(val value: String) : CadastroEvent()
    data class OnPesoChanged(val value: String) : CadastroEvent()
    data class OnVacinadoChanged(val value: Boolean) : CadastroEvent()

    data class OnCodigoLoteChanged(val value: String) : CadastroEvent()
    data class OnQuantidadeChanged(val value: String) : CadastroEvent()
    data class OnPrecoChanged(val value: String) : CadastroEvent()

    data class LoadProduto(val id: String) : CadastroEvent()

    object Submit : CadastroEvent()
    object ClearError : CadastroEvent()
}

//Tudo o que a tela precisa MOSTRAR
data class CadastroUiState(
    val isLoading: Boolean = false,
    val isAnimalMode: Boolean = true,
    val photoUris: List<Uri> = emptyList(),
    val endereco: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val isEditMode: Boolean = false,
    val existingId: String? = null,

    // Inputs do Animal
    val especie: String = "Ovino",
    val brinco: String = "",
    val nome: String = "",
    val sexo: String = "Fêmea",
    val raca: String = "",
    val peso: String = "",
    val isVacinado: Boolean = false,

    // Inputs do Derivado
    val codigoLote: String = "",
    val quantidade: String = "",
    val preco: String = "",
    val productType: String = "",

    // Listas padronizadas para os Dropdowns (menus de seleção)
    val especies: List<String> = listOf("Ovino", "Bovino", "Caprino", "Suíno", "Equino", "Outro"),
    val tiposDerivado: List<String> = listOf("Leite", "Queijo", "Carne", "Manta de Carneiro", "Lã", "Pele", "Iogurte", "Manteiga", "Outro"),
    val unidadesMedida: List<String> = listOf("KG", "G", "Litro", "mL", "UND"),

    // Unidade inteligente que vai mudar sozinha (Ex: Escolheu Leite -> vira Litro)
    val suggestedUnit: String = "UND"
)
