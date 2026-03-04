package com.example.conectaovinos.database.entities

data class MarketplaceItemUi(
    val id: Int,
    val nome: String,
    val custo: Double,
    val categoria: String, // "Animais", "Derivados", "Equipamentos"
    val raca: String? = null,
    val fotoUri: String?

)