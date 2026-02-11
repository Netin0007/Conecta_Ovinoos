package com.example.conectaovinos.models

import com.example.conectaovinos.database.entities.AnimalEntity

data class Animal(
    override val id: String,
    override val nome: String,
    val raca: String,
    val dataNascimento: String,
    override val custo: Double,
    val fotoUri: String?
) : Produto(id, nome, custo)

fun AnimalEntity.toAnimalModel(): Animal {
    return Animal(
        id = id.toString(),
        nome = nome,
        raca = raca,
        dataNascimento = idade.toString(),
        custo = preco,
        fotoUri = fotoUri
    )
}
