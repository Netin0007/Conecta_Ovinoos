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
    imageUrls = imageUrls.split(",").filter { it.isNotEmpty() },
    ativo = ativo,
    dataCriacao = Date(dataCriacaoMs),
    latitude = latitude,
    longitude = longitude,
    endereco = endereco
)

fun Anuncio.toEntity() = AnuncioEntity(
    id = id,
    animalId = animalId,
    nomeAnimal = nomeAnimal,
    racaAnimal = racaAnimal,
    custoAnimal = custoAnimal,
    precoVenda = precoVenda,
    descricao = descricao,
    imageUrls = imageUrls.joinToString(","),
    ativo = ativo,
    dataCriacaoMs = dataCriacao.time,
    latitude = latitude,
    longitude = longitude,
    endereco = endereco
)