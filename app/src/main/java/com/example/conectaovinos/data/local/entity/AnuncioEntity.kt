package com.example.conectaovinos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anuncios")
data class AnuncioEntity(
    @PrimaryKey val id: String,
    val animalId: String,
    val nomeAnimal: String,
    val racaAnimal: String,
    val custoAnimal: Double,
    val precoVenda: Double,
    val descricao: String,
    val ativo: Boolean = true,
    val dataCriacaoMs: Long
)