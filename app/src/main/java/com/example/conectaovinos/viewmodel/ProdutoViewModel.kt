package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.entities.ProdutoEntity
import com.example.conectaovinos.database.repository.ProdutoRepository
import kotlinx.coroutines.launch

class ProdutoViewModel(private val repository: ProdutoRepository) : ViewModel() {

    fun addProduto(produto: ProdutoEntity) {
        viewModelScope.launch {
            repository.insertProduto(produto)
        }
    }
}
