package com.example.conectaovinos.data.local.mapper

import com.example.conectaovinos.data.local.entity.AnimalEntity
import com.example.conectaovinos.data.local.entity.ProdutoDerivadoEntity
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado

fun AnimalEntity.toModel() = AnimalLote(
    id = id,
    custoTotal = custoTotal,
    dataRegistro = dataRegistro,
    imageUrls = imageUrls.split(",").filter { it.isNotEmpty() },
    especie = especie,
    quantidade = quantidade,
    nome = nome,
    brinco = brinco,
    raca = raca,
    sexo = sexo,
    peso = peso,
    vacinado = vacinado,
    latitude = latitude,
    longitude = longitude,
    endereco = endereco
)

fun AnimalLote.toEntity() = AnimalEntity(
    id = id,
    custoTotal = custoTotal,
    dataRegistro = dataRegistro,
    imageUrls = imageUrls.joinToString(","),
    especie = especie,
    quantidade = quantidade,
    nome = nome,
    brinco = brinco,
    raca = raca,
    sexo = sexo,
    peso = peso,
    vacinado = vacinado,
    latitude = latitude,
    longitude = longitude,
    endereco = endereco
)

fun ProdutoDerivadoEntity.toModel() = ProdutoProcessado(
    id = id,
    custoTotal = custoTotal,
    dataRegistro = dataRegistro,
    imageUrls = imageUrls.split(",").filter { it.isNotEmpty() },
    tipoProduto = tipoProduto,
    unidadeMedida = unidadeMedida,
    quantidade = quantidade,
    codigoLote = codigoLote,
    latitude = latitude,
    longitude = longitude,
    endereco = endereco
)

fun ProdutoProcessado.toEntity() = ProdutoDerivadoEntity(
    id = id,
    custoTotal = custoTotal,
    dataRegistro = dataRegistro,
    imageUrls = imageUrls.joinToString(","),
    tipoProduto = tipoProduto,
    unidadeMedida = unidadeMedida,
    quantidade = quantidade,
    codigoLote = codigoLote,
    latitude = latitude,
    longitude = longitude,
    endereco = endereco
)