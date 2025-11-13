package com.example.conectaovinos.models

import java.util.Date

enum class TipoManejo {
    Vacinacao,
    Pesagem,
    Tratamento,
    Reproducao,
    Alimentacao
}

data class EventoManejo(
    val id: String,
    val tipo: TipoManejo,
    val data: Date,
    val descricao: String,
    val animalId: String
)