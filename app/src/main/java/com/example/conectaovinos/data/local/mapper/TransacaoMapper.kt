package com.example.conectaovinos.data.local.mapper

import com.example.conectaovinos.data.local.entity.TransacaoEntity
import com.example.conectaovinos.models.TipoTransacao
import com.example.conectaovinos.models.Transacao
import java.util.Date

fun TransacaoEntity.toModel() = Transacao(
    id = id,
    descricao = descricao,
    valor = valor,
    tipo = if (tipo == "Receita") TipoTransacao.Receita else TipoTransacao.Despesa,
    data = Date(dataMs),
    categoria = categoria
)

fun Transacao.toEntity() = TransacaoEntity(
    id = id,
    descricao = descricao,
    valor = valor,
    tipo = if (tipo == TipoTransacao.Receita) "Receita" else "Despesa",
    dataMs = data.time,
    categoria = categoria
)