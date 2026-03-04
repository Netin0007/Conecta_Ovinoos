package com.example.conectaovinos.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.conectaovinos.database.enums.tipoTransacao


@Entity(tableName = "transacoes")
data class TransacaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipo: tipoTransacao, // "RECEITA" ou "DESPESA"
    val descricao: String,
    val valor: Double,
    val categoria: String,
    val data: String
)
