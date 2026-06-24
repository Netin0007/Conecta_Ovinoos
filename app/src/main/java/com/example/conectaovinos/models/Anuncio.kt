package com.example.conectaovinos.models

import java.util.Date

data class Anuncio(
    val id: String,
    val animalId: String,
    val nomeAnimal: String,
    val racaAnimal: String,
    val custoAnimal: Double,
    val precoVenda: Double,
    val descricao: String,
    val imageUrls: List<String> = emptyList(),
    val ativo: Boolean,
    val dataCriacao: Date,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val endereco: String = ""
)