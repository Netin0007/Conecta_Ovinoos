package com.example.conectaovinos

import androidx.compose.runtime.mutableStateListOf
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoProcessado

/*
// Dados de teste atualizados para o novo chassi da fazenda
val rebanhoGlobal = mutableStateListOf<Produto>(
    AnimalLote(
        id = "1",
        especie = "Ovino",
        quantidade = 10, // Transformamos a Mococa num lote de 10 cabeças
        custoTotal = 2500.0
    ),
    ProdutoProcessado(
        id = "p1",
        tipoProduto = "Queijo de Cabra",
        unidadeMedida = "Unidade",
        quantidade = 5.0,
        custoTotal = 75.0
    ),
    AnimalLote(
        id = "2",
        especie = "Bovino",
        quantidade = 5,
        custoTotal = 15000.0
    ),
    AnimalLote(
        id = "3",
        especie = "Caprino",
        quantidade = 15,
        custoTotal = 3300.0
    )
)
*/
val rebanhoGlobal = mutableStateListOf<Produto>()
