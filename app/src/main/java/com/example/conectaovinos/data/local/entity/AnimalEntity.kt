package com.example.conectaovinos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animais")
data class AnimalEntity(
    @PrimaryKey val id: String,
    val custoTotal: Double,
    val dataRegistro: Long,
    val imageUrls: String = "", // Lista de URLs separadas por vírgula
    val especie: String,
    val quantidade: Int,
    val nome: String,
    val brinco: String,
    val raca: String,
    val sexo: String,
    val peso: Double,
    val vacinado: Boolean,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val endereco: String = ""
)