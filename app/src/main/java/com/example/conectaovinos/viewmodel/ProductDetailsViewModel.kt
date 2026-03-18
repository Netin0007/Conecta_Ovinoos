package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.dao.ProdutoDao
import com.example.conectaovinos.database.entities.ProdutosEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProductDetailsViewModel(
    produtoDao: ProdutoDao,
    produtoId: Int
) : ViewModel() {

    val produto: StateFlow<ProdutosEntity?> =
        produtoDao.getById(produtoId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

class ProductDetailsViewModelFactory(
    private val produtoDao: ProdutoDao,
    private val produtoId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductDetailsViewModel(produtoDao, produtoId) as T
    }
}