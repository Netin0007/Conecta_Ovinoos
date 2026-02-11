package com.example.conectaovinos.database.repository

import com.example.conectaovinos.database.dao.ManejoDao
import com.example.conectaovinos.database.entities.ManejoEntity
import kotlinx.coroutines.flow.Flow

class ManejoRepository(private val dao: ManejoDao) {

    val manejos = dao.getAllManejos()

    fun getManejosByAnimal(animalId: Int?): Flow<List<ManejoEntity>> {
        return dao.getManejoByAnimals(animalId)
    }

    suspend fun insertManejo(manejo: ManejoEntity) {
        dao.addManejo(manejo)
    }

    suspend fun deleteManejo(manejo: ManejoEntity) {
        dao.deleteManejo(manejo)
    }
}
