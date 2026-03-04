package com.example.conectaovinos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.InventoryItem
import com.example.conectaovinos.database.DatabaseProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class InventoryViewModel(app: Application) : AndroidViewModel(app) {

    private val db = DatabaseProvider.get(app)
    private val animalDao = db.animalDao()
    private val produtosDao = db.produtosDao()

    val items = combine(
        animalDao.getAll(),
        produtosDao.getAll()
    ) { animais, produtos ->
        val a = animais.map { InventoryItem.AnimalItem(it) }
        val p = produtos.map { InventoryItem.ProdutoItem(it) }
        a + p
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}