package com.example.conectaovinos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animais")
data class AnimalEntity(
    @PrimaryKey val id: String,
    val custoTotal: Double,
    val dataRegistro: Long,
    val especie: String,
    val quantidade: Int
)