package com.example.conectaovinos.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "animals")
data class AnimalEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val raca: String,
    val idade: Int,
    val preco: Double
)