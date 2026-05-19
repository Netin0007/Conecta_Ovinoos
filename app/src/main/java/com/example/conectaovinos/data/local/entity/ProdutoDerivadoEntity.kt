package com.example.conectaovinos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos_derivados")
data class ProdutoDerivadoEntity(
    @PrimaryKey val id: String,
    val custoTotal: Double,
    val dataRegistro: Long,
    val tipoProduto: String,
    val unidadeMedida: String,
    val quantidade: Double
)