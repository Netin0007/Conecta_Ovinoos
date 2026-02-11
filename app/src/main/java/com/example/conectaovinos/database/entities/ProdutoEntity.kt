package com.example.conectaovinos.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class ProdutoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quantidade: Int,
    val nome: String,
    val preco: Double,
    val unidade: String
)
