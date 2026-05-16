package com.example.conectaovinos.data.local.dao

import androidx.room.*
import com.example.conectaovinos.data.local.entity.AnuncioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnuncioDao {

    @Query("SELECT * FROM anuncios WHERE ativo = 1 ORDER BY dataCriacaoMs DESC")
    fun observarAnunciosAtivos(): Flow<List<AnuncioEntity>>

    @Query("SELECT * FROM anuncios ORDER BY dataCriacaoMs DESC")
    fun observarTodosAnuncios(): Flow<List<AnuncioEntity>>

    @Query("SELECT * FROM anuncios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: String): AnuncioEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM anuncios WHERE animalId = :animalId AND ativo = 1)")
    fun isAnimalAnunciado(animalId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(anuncio: AnuncioEntity)

    @Query("UPDATE anuncios SET ativo = :ativo WHERE id = :id")
    suspend fun atualizarAtivo(id: String, ativo: Boolean)

    @Delete
    suspend fun deletar(anuncio: AnuncioEntity)
}