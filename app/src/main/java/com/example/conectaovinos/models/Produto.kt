package com.example.conectaovinos.models

// Classe base (pai)
open class Produto(
    open val id: String,
    open val nome: String,
    open val custo: Double
)
