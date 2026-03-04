package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.dao.AnimalDao
import com.example.conectaovinos.database.dao.ProdutoDao
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.entities.MarketplaceItemUi
import com.example.conectaovinos.database.entities.ProdutosEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MarketplaceViewModel(
    private val animalDao: AnimalDao,
    private val derivadoDao: ProdutoDao
) : ViewModel() {

    val itens: StateFlow<List<MarketplaceItemUi>> =
        combine(
            animalDao.getAll(),
            derivadoDao.getAll()
        ) { animais: List<AnimalEntity>, derivados: List<ProdutosEntity> ->

            val animaisUi = animais.map { a ->
                // ✅ AJUSTE NOMES DE CAMPOS SE PRECISAR
                MarketplaceItemUi(
                    id = a.id,
                    nome = a.nome,
                    custo = a.preco,
                    categoria = "Animais",
                    fotoUri = a.fotoUri // se não existir no seu AnimalEntity, coloque null
                )
            }

            val derivadosUi = derivados.map { d ->
                MarketplaceItemUi(
                    id = d.id,
                    nome = d.nome,
                    custo = d.preco,
                    categoria = "Derivados",
                    fotoUri = d.fotoUri
                )
            }

            // Junta tudo
            animaisUi + derivadosUi
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}