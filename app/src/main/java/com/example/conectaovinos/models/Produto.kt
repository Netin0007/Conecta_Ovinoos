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
    val imageUrls: List<String> // Lista de URLs ou Caminhos das fotos
    val latitude: Double?
    val longitude: Double?
    val endereco: String
}

/**
 * Representa um LOTE de Animais Vivos (Ovinos, Bovinos, Caprinos, etc.)
 */
data class AnimalLote(
    override val id: String,
    override val custoTotal: Double,
    override val dataRegistro: Long = System.currentTimeMillis(),
    override val imageUrls: List<String> = emptyList(),
    val especie: String, // Ex: "Ovino", "Bovino", "Caprinos"
    val quantidade: Int, // Essencial para saber o custo por cabeça
    val nome: String = "",
    val brinco: String = "",
    val raca: String = "",
    val sexo: String = "",
    val peso: Double = 0.0,
    val vacinado: Boolean = false,
    override val latitude: Double? = null,
    override val longitude: Double? = null,
    override val endereco: String = ""
) : Produto {
    // O nome amigavel é gerado dinamicamente
    override val nomeAmigavel: String
        get() = if (nome.isNotBlank()) nome.uppercase() else "Lote de ${especie}s ($quantidade un.)"
}

/**
 * Representa produtos processados ou derivados (Queijo, KG da Carne, etc.)
 */
data class ProdutoProcessado(
    override val id: String,
    override val custoTotal: Double,
    override val dataRegistro: Long = System.currentTimeMillis(),
    override val imageUrls: List<String> = emptyList(),
    val tipoProduto: String, // Ex: "Queijo", "Manta", "KG da Carne (Carcaca)"
    val unidadeMedida: String, // Ex: "Kg", "Unidade"
    val quantidade: Double, // Pode ser quebrado ex: 10.5 Kg
    val codigoLote: String = "",
    override val latitude: Double? = null,
    override val longitude: Double? = null,
    override val endereco: String = ""
) : Produto {
    // Ex: "Queijo (10.0 Unidade)" ou "KG da Carne (50.5 Kg)"
    override val nomeAmigavel: String
        get() = "$tipoProduto ($quantidade $unidadeMedida)"
}
