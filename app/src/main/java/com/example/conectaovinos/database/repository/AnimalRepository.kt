package com.example.conectaovinos.database.repository

import com.example.conectaovinos.database.dao.AnimalDao
import com.example.conectaovinos.database.entities.AnimalEntity

class AnimalRepository(private val dao: AnimalDao) {

    val animals = dao.getall()

    suspend fun insert(animal: AnimalEntity){
        dao.insert(animal)
    }

}