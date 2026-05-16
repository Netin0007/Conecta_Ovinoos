package com.example.conectaovinos.data

import com.example.conectaovinos.data.local.dao.RebanhoDao
import com.example.conectaovinos.data.local.entity.AnimalEntity
import com.example.conectaovinos.data.local.entity.ProdutoDerivadoEntity
import com.example.conectaovinos.data.local.mapper.toEntity
import com.example.conectaovinos.data.local.mapper.toModel
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoDerivado
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * Fonte de verdade para rebanho e produtos.
 * A interface pública é idêntica à versão sem Room —
 * os ViewModels não precisam saber que agora existe um banco.
 */
class RebanhoRepository(private val dao: RebanhoDao) {

    // Combina os dois flows do DAO num único Flow<List<Produto>> para a UI
    val produtos: Flow<List<Produto>> = combine(
        dao.observarAnimais(),
        dao.observarDerivados()
    ) { animais, derivados ->
        val lista = mutableListOf<Produto>()
        lista.addAll(animais.map { it.toModel() })
        lista.addAll(derivados.map { it.toModel() })
        lista.sortedBy { it.nome }
    }

    suspend fun addAnimal(animal: Animal) {
        dao.inserirAnimal(animal.toEntity())
    }

    suspend fun addProdutoDerivado(derivado: ProdutoDerivado) {
        dao.inserirDerivado(derivado.toEntity())
    }

    suspend fun removeProduto(id: String) {
        // Tenta em ambas as tabelas — só uma vai encontrar
        dao.buscarAnimalPorId(id)?.let { dao.deletarAnimal(it) }
        dao.buscarDerivadoPorId(id)?.let { dao.deletarDerivado(it) }
    }

    suspend fun getProdutoById(id: String): Produto? {
        return dao.buscarAnimalPorId(id)?.toModel()
    }
}