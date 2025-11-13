package com.example.conectaovinos.models

sealed interface Produto {
    val id: String
    val nome: String
    val fotoUrl: String?
    val custo: Double
}

data class Animal(
    override val id: String,
    override val nome: String, // Antes era 'identification'
    override val fotoUrl: String? = null,
    override val custo: Double,
    val raca: String,
    val dataNascimento: String
) : Produto

data class ProdutoDerivado(
    override val id: String,
    override val nome: String,
    override val fotoUrl: String? = null,
    override val custo: Double,
    val unidadeDeMedida: String
) : Produto