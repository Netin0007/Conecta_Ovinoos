package com.example.conectaovinos.models

import java.util.Date

// Modelo para a coleção "animals" no Firebase
data class AnimalModel(
    val id: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val earTag: String = "",
    val animalType: String = "",
    val breed: String = "",
    val sex: String = "",
    val birthDate: Long = 0L,
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val length: Double = 0.0,
    val vaccineStatus: Boolean = false,
    val dewormedStatus: Boolean = false,
    val observations: String = "",
    val availableForSale: Boolean = false,
    val salePrice: Double = 0.0,
    val status: String = "Ativo",
    val createdAt: Date? = null
)

// Modelo para a coleção "products" no Firebase
data class DerivadoModel(
    val id: String = "",
    val imageUrl: String = "",
    val batchCode: String = "",
    val productType: String = "",
    val unit: String = "",
    val quantity: Double = 0.0,
    val totalWeight: Double = 0.0,
    val productionDate: Long = 0L,
    val expirationDate: Long = 0L,
    val salePrice: Double = 0.0,
    val stockStatus: String = "Em estoque",
    val observations: String = "",
    val createdAt: Date? = null
)