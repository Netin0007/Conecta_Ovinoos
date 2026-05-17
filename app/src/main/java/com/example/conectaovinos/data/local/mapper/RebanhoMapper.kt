package com.example.conectaovinos.data.local.mapper

import com.example.conectaovinos.data.local.entity.AnimalEntity
import com.example.conectaovinos.data.local.entity.ProdutoDerivadoEntity
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.ProdutoDerivado

fun AnimalEntity.toModel() = Animal(
    id = id,
    nome = nome,
    raca = raca,
    dataNascimento = dataNascimento,
    custo = custo,
    fotoUrl = fotoUrl
)

fun Animal.toEntity() = AnimalEntity(
    id = id,
    nome = nome,
    raca = raca,
    dataNascimento = dataNascimento,
    custo = custo,
    fotoUrl = fotoUrl
)

fun ProdutoDerivadoEntity.toModel() = ProdutoDerivado(
    id = id,
    nome = nome,
    unidadeDeMedida = unidadeDeMedida,
    custo = custo,
    fotoUrl = fotoUrl
)

fun ProdutoDerivado.toEntity() = ProdutoDerivadoEntity(
    id = id,
    nome = nome,
    unidadeDeMedida = unidadeDeMedida,
    custo = custo,
    fotoUrl = fotoUrl
)