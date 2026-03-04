package com.example.conectaovinos.viewmodel


import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.DatabaseProvider
import com.example.conectaovinos.database.dao.ProdutoDao
import com.example.conectaovinos.database.entities.ProdutosEntity

import kotlinx.coroutines.launch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.enums.tipoProduto
import kotlinx.coroutines.launch

class AddItemViewModel(app: Application) : AndroidViewModel(app) {

    private val db = DatabaseProvider.get(app)
    private val animalDao = db.animalDao()
    private val produtosDao = db.produtosDao()

    fun salvarAnimal(
        nome: String,
        raca: String,
        precoText: String,
        fotoUri: String? = null
    ) {
        val preco = precoText.replace(",", ".").toDoubleOrNull() ?: 0.0

        val entity = AnimalEntity(
            nome = nome.trim(),
            raca = raca.trim(),
            preco = preco,
            fotoUri = fotoUri
        )

        viewModelScope.launch {
            animalDao.insert(entity)
        }
    }

    fun salvarProduto(
        nome: String,
        tipo: tipoProduto,
        precoText: String,
        quantidade: String,
        fotoUri: String?
    ) {
        val preco = precoText.replace(",", ".").toDoubleOrNull() ?: 0.0

        val entity = ProdutosEntity(
            nome = nome.trim(),
            tipo = tipo,
            preco = preco,
            quantidade = quantidade.trim(),
            fotoUri = fotoUri
        )

        viewModelScope.launch {
            produtosDao.addProduto(entity)
        }
    }
}