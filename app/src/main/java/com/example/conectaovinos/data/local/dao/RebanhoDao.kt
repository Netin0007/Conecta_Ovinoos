package com.example.conectaovinos.data.local.dao

import androidx.room.*
import com.example.conectaovinos.data.local.entity.AnimalEntity
import com.example.conectaovinos.data.local.entity.ProdutoDerivadoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RebanhoDao {

    // --- Animais ---

    @Query("SELECT * FROM animais ORDER BY nome ASC")
    fun observarAnimais(): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animais WHERE id = :id LIMIT 1")
    suspend fun buscarAnimalPorId(id: String): AnimalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirAnimal(animal: AnimalEntity)

    @Delete
    suspend fun deletarAnimal(animal: AnimalEntity)

    // --- Produtos derivados ---

    @Query("SELECT * FROM produtos_derivados ORDER BY nome ASC")
    fun observarDerivados(): Flow<List<ProdutoDerivadoEntity>>

    @Query("SELECT * FROM produtos_derivados WHERE id = :id LIMIT 1")
    suspend fun buscarDerivadoPorId(id: String): ProdutoDerivadoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirDerivado(derivado: ProdutoDerivadoEntity)

    @Delete
    suspend fun deletarDerivado(derivado: ProdutoDerivadoEntity)
}