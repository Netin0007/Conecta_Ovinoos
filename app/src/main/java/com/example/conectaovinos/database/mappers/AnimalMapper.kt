package com.example.conectaovinos.database.mappers

import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.models.Animal

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
