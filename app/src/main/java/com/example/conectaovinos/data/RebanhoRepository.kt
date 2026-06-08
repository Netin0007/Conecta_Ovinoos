package com.example.conectaovinos.data

import com.example.conectaovinos.data.local.dao.RebanhoDao
import com.example.conectaovinos.data.local.mapper.toEntity
import com.example.conectaovinos.data.local.mapper.toModel
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoProcessado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Fonte de verdade para o inventário da fazenda.
 * Adaptado para suportar múltiplos tipos de animais e produtos processados.
 */
class RebanhoRepository @Inject constructor(private val dao: RebanhoDao) {

    // Combina os dois flows do DAO num único Flow<List<Produto>> para a UI
    val produtos: Flow<List<Produto>> = combine(
        dao.observarAnimais(),
        dao.observarDerivados()
    ) { animais, derivados ->
        val lista = mutableListOf<Produto>()
        lista.addAll(animais.map { it.toModel() })
        lista.addAll(derivados.map { it.toModel() })
        // Ordena pelos itens recém-adicionados no topo
        lista.sortedByDescending { it.dataRegistro }
    }

    suspend fun addAnimalLote(lote: AnimalLote) {
        dao.inserirAnimal(lote.toEntity())
    }

    suspend fun updateAnimalLote(lote: AnimalLote) {
        dao.atualizarAnimal(lote.toEntity())
    }

    suspend fun addProdutoProcessado(produto: ProdutoProcessado) {
        dao.inserirDerivado(produto.toEntity())
    }

    suspend fun removeProduto(id: String) {
        // Tenta remover em ambas as tabelas
        dao.buscarAnimalPorId(id)?.let { dao.deletarAnimal(it) }
        dao.buscarDerivadoPorId(id)?.let { dao.deletarDerivado(it) }
    }

    suspend fun getProdutoById(id: String): Produto? {
        // Retorna o produto se achar (útil para a tela de edição)
        return dao.buscarAnimalPorId(id)?.toModel()
            ?: dao.buscarDerivadoPorId(id)?.toModel()
    }
}