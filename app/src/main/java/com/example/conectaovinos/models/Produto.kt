// ARQUIVO: com/example/conectaovinos/models/Produto.kt (ou Animal.kt)

package com.example.conectaovinos.models

/**
 * Interface SELADA base para qualquer produto que vai para o inventário.
 * Usamos Sealed Interface para o Kotlin garantir total segurança nas
has reativas e listagens.
 */
sealed interface Produto {
    val id: String
    val nomeAmigavel: String // Nome que aparece na lista
    val custoTotal: Double   // Quanto o produtor gastou no lote
    val dataRegistro: Long   // Timestamp para ordenação
}

/**
 * Representa um LOTE de Animais Vivos (Ovinos, Bovinos, Caprinos, etc.)
 */
data class AnimalLote(
    override val id: String,
    override val custoTotal: Double,
    override val dataRegistro: Long = System.currentTimeMillis(),
    val especie: String, // Ex: "Ovino", "Bovino", "Caprino"
    val quantidade: Int // Essencial para saber o custo por cabeça
) : Produto {
    // O nome amigavel é gerado automaticamente ex: "Lote de Ovinos (25 un.)"
    override val nomeAmigavel: String
        get() = "Lote de ${especie}s ($quantidade un.)"
}

/**
 * Representa produtos processados ou derivados (Queijo, KG da Carne, etc.)
 */
data class ProdutoProcessado(
    override val id: String,
    override val custoTotal: Double,
    override val dataRegistro: Long = System.currentTimeMillis(),
    val tipoProduto: String, // Ex: "Queijo", "Manta", "KG da Carne (Carcaca)"
    val unidadeMedida: String, // Ex: "Kg", "Unidade"
    val quantidade: Double // Pode ser quebrado ex: 10.5 Kg
) : Produto {
    // Ex: "Queijo (10.0 Unidade)" ou "KG da Carne (50.5 Kg)"
    override val nomeAmigavel: String
        get() = "$tipoProduto ($quantidade $unidadeMedida)"
}