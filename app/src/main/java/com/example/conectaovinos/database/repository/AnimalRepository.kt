package com.example.conectaovinos.database.repository

import com.example.conectaovinos.database.dao.AnimalDao
import com.example.conectaovinos.database.entities.AnimalEntity

class AnimalRepository(private val dao: AnimalDao) {

    val animals = dao.getAll()

    suspend fun insert(animal: AnimalEntity) {
        dao.insert(animal)
    }

    suspend fun getAnimalById(id: Int?): AnimalEntity? {
        return dao.getAnimalById(id)
    }

    suspend fun updateAnimal(animal: AnimalEntity) {
        dao.updateAnimal(animal)
    }

    suspend fun deleteAnimal(animal: AnimalEntity) {
        dao.deleteAnimal(animal)
    }

    suspend fun deleteAllAnimals() {
        dao.deleteAllAnimals()
    }
}
