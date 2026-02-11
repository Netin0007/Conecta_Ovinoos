package com.example.conectaovinos.models

data class ProdutoDerivado(
    override val id: String,
    override val nome: String,
    val unidadeDeMedida: String,
    override val custo: Double
) : Produto(id, nome, custo)
