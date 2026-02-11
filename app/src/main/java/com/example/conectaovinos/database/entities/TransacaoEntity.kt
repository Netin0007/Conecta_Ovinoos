package com.example.conectaovinos.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "transacoes")
data class TransacaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipo: String, // "RECEITA" ou "DESPESA"
    val descricao: String,
    val valor: Double,
    val categoria: String,
    val data: String
)
