package com.example.conectaovinos

import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.entities.ProdutosEntity

sealed class InventoryItem {
    data class AnimalItem(val animal: AnimalEntity) : InventoryItem()
    data class ProdutoItem(val produto: ProdutosEntity) : InventoryItem()
}