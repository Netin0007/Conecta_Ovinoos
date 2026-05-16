package com.example.conectaovinos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos_derivados")
data class ProdutoDerivadoEntity(
    @PrimaryKey val id: String,
    val nome: String,
    val unidadeDeMedida: String,
    val custo: Double,
    val fotoUrl: String? = null
)