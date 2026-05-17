package com.example.conectaovinos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animais")
data class AnimalEntity(
    @PrimaryKey val id: String,
    val nome: String,
    val raca: String,
    val dataNascimento: String,
    val custo: Double,
    val fotoUrl: String? = null
)