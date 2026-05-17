package com.example.conectaovinos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacoes")
data class TransacaoEntity(
    @PrimaryKey val id: String,
    val descricao: String,
    val valor: Double,
    val tipo: String,      // "Receita" ou "Despesa" — String simples, sem enum
    val dataMs: Long,      // Date salva como milissegundos (Long é fácil para o Room)
    val categoria: String
)
