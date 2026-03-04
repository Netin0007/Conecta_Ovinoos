package com.example.conectaovinos.database.repository

import com.example.conectaovinos.database.dao.ManejoDao
import com.example.conectaovinos.database.entities.ManejoEntity
import kotlinx.coroutines.flow.Flow

class ManejoRepository(private val dao: ManejoDao) {
    suspend fun insert(manejo: ManejoEntity) = dao.addManejo(manejo)
    fun maneiosDoAnimal(animalId: String) = dao.getManejosDoAnimal(animalId)
}