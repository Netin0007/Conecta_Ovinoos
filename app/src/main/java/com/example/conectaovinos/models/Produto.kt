package com.example.conectaovinos.models

// O "CONTRATO"
sealed interface Produto {
    val id: String
    val nome: String
    val fotoUrl: String?
    val custo: Double
}

// TIPO ESPECÍFICO 1: ANIMAL
data class Animal(
    override val id: String,
    override val nome: String, // Antes era 'identification'
    override val fotoUrl: String? = null,
    override val custo: Double,
    val raca: String,
    val dataNascimento: String
) : Produto

// TIPO ESPECÍFICO 2: PRODUTO DERIVADO
data class ProdutoDerivado(
    override val id: String,
    override val nome: String,
    override val fotoUrl: String? = null,
    override val custo: Double,
    val unidadeDeMedida: String
) : Produto