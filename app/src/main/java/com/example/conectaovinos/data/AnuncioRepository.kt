package com.example.conectaovinos.data

import com.example.conectaovinos.data.local.dao.AnuncioDao
import com.example.conectaovinos.data.local.mapper.toEntity
import com.example.conectaovinos.data.local.mapper.toModel
import com.example.conectaovinos.models.Anuncio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnuncioRepository(private val dao: AnuncioDao) {

    /** Apenas anúncios ativos — usado pelo Marketplace */
    val anunciosAtivos: Flow<List<Anuncio>> = dao
        .observarAnunciosAtivos()
        .map { it.map { entity -> entity.toModel() } }

    /** Todos os anúncios (ativos e pausados) — usado pela tela de gestão do produtor */
    val todosAnuncios: Flow<List<Anuncio>> = dao
        .observarTodosAnuncios()
        .map { it.map { entity -> entity.toModel() } }

    fun isAnimalAnunciado(animalId: String): Flow<Boolean> =
        dao.isAnimalAnunciado(animalId)

    suspend fun publicar(anuncio: Anuncio) {
        dao.inserir(anuncio.toEntity())
    }

    suspend fun pausar(id: String) {
        dao.atualizarAtivo(id, false)
    }

    suspend fun reativar(id: String) {
        dao.atualizarAtivo(id, true)
    }

    suspend fun buscarPorId(id: String): Anuncio? {
        return dao.buscarPorId(id)?.toModel()
    }

    suspend fun deletar(id: String) {
        dao.buscarPorId(id)?.let { dao.deletar(it) }
    }
}