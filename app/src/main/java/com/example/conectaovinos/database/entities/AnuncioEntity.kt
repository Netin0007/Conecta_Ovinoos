package com.example.conectaovinos.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anuncios")
data class AnuncioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val animalId: Int,
    val price: Double,
    val description: String,
    val status: String = "Ativo"
)