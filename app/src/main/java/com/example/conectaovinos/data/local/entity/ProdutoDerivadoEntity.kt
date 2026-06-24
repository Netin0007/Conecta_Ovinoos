package com.example.conectaovinos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos_derivados")
data class ProdutoDerivadoEntity(
    @PrimaryKey val id: String,
    val custoTotal: Double,
    val dataRegistro: Long,
    val imageUrls: String = "", // Lista de URLs separadas por vírgula
    val tipoProduto: String,
    val unidadeMedida: String,
    val quantidade: Double,
    val codigoLote: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val endereco: String = ""
)