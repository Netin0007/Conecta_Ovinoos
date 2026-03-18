package com.example.conectaovinos.database.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.conectaovinos.database.enums.tipoProduto


@Entity(tableName = "produtosTable")
data class ProdutosEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val tipo: tipoProduto,
    val preco: Double,
    val quantidade: String,
    val fotoUri: String?= null,
    val isFavorite: Boolean = false
)