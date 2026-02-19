package com.example.conectaovinos

import androidx.compose.runtime.mutableStateListOf
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoDerivado

val rebanhoGlobal = mutableStateListOf<Produto>(
    Animal(id = "1", nome = "Mococa 01", raca = "Santa Inês", dataNascimento = "10/05/2023", custo = 250.0),
    ProdutoDerivado(id = "p1", nome = "Queijo de Cabra", unidadeDeMedida = "Peça de 500g", custo = 15.0),
    Animal(id = "2", nome = "Brinco 142", raca = "Dorper", dataNascimento = "02/01/2024", custo = 300.0),
    Animal(id = "3", nome = "Fumacinha", raca = "SRD", dataNascimento = "25/08/2022", custo = 220.0)
)