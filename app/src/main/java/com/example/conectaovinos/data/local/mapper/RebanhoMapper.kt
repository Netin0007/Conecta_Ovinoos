package com.example.conectaovinos.data.local.mapper

import com.example.conectaovinos.data.local.entity.AnimalEntity
import com.example.conectaovinos.data.local.entity.ProdutoDerivadoEntity
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado

fun AnimalEntity.toModel() = AnimalLote(
    id = id,
    custoTotal = custoTotal,
    dataRegistro = dataRegistro,
    especie = especie,
    quantidade = quantidade
)

fun AnimalLote.toEntity() = AnimalEntity(
    id = id,
    custoTotal = custoTotal,
    dataRegistro = dataRegistro,
    especie = especie,
    quantidade = quantidade
)

fun ProdutoDerivadoEntity.toModel() = ProdutoProcessado(
    id = id,
    custoTotal = custoTotal,
    dataRegistro = dataRegistro,
    tipoProduto = tipoProduto,
    unidadeMedida = unidadeMedida,
    quantidade = quantidade
)

fun ProdutoProcessado.toEntity() = ProdutoDerivadoEntity(
    id = id,
    custoTotal = custoTotal,
    dataRegistro = dataRegistro,
    tipoProduto = tipoProduto,
    unidadeMedida = unidadeMedida,
    quantidade = quantidade
)