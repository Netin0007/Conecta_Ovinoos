package com.example.conectaovinos.data.local.mapper

import com.example.conectaovinos.data.local.entity.AnuncioEntity
import com.example.conectaovinos.models.Anuncio
import java.util.Date

fun AnuncioEntity.toModel() = Anuncio(
    id = id,
    animalId = animalId,
    nomeAnimal = nomeAnimal,
    racaAnimal = racaAnimal,
    custoAnimal = custoAnimal,
    precoVenda = precoVenda,
    descricao = descricao,
    ativo = ativo,
    dataCriacao = Date(dataCriacaoMs)
)

fun Anuncio.toEntity() = AnuncioEntity(
    id = id,
    animalId = animalId,
    nomeAnimal = nomeAnimal,
    racaAnimal = racaAnimal,
    custoAnimal = custoAnimal,
    precoVenda = precoVenda,
    descricao = descricao,
    ativo = ativo,
    dataCriacaoMs = dataCriacao.time
)