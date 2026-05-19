package com.example.conectaovinos.data.local.dao

import androidx.room.*
import com.example.conectaovinos.data.local.entity.AnimalEntity
import com.example.conectaovinos.data.local.entity.ProdutoDerivadoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RebanhoDao {

    // --- Animais ---
    // CORREÇÃO: Agora ordenamos pela dataRegistro (do mais novo para o mais velho)
    @Query("SELECT * FROM animais ORDER BY dataRegistro DESC")
    fun observarAnimais(): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animais WHERE id = :id LIMIT 1")
    suspend fun buscarAnimalPorId(id: String): AnimalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirAnimal(animal: AnimalEntity)

    @Update
    suspend fun atualizarAnimal(animal: AnimalEntity)

    @Delete
    suspend fun deletarAnimal(animal: AnimalEntity)

    // --- Produtos Derivados / Carnes ---
    // CORREÇÃO: Ordenação corrigida aqui também
    @Query("SELECT * FROM produtos_derivados ORDER BY dataRegistro DESC")
    fun observarDerivados(): Flow<List<ProdutoDerivadoEntity>>

    @Query("SELECT * FROM produtos_derivados WHERE id = :id LIMIT 1")
    suspend fun buscarDerivadoPorId(id: String): ProdutoDerivadoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirDerivado(derivado: ProdutoDerivadoEntity)

    // Já deixei o Update preparado aqui para quando formos editar os queijos e carnes!
    @Update
    suspend fun atualizarDerivado(derivado: ProdutoDerivadoEntity)

    @Delete
    suspend fun deletarDerivado(derivado: ProdutoDerivadoEntity)
}