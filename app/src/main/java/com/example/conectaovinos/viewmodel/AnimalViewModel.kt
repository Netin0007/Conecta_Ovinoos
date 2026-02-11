package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.repository.AnimalRepository
import kotlinx.coroutines.launch


class AnimalViewModel(private val repository: AnimalRepository) : ViewModel() {

    val animals = repository.animals

    fun addAnimal(animal: AnimalEntity) {
        viewModelScope.launch {
            repository.insert(animal)
        }
    }

    fun updateAnimal(animal: AnimalEntity) {
        viewModelScope.launch {
            repository.updateAnimal(animal)
        }
    }

    fun deleteAnimal(animal: AnimalEntity) {
        viewModelScope.launch {
            repository.deleteAnimal(animal)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAllAnimals()
        }
    }

    suspend fun getAnimalById(id: Int?): AnimalEntity? {
        return repository.getAnimalById(id)
    }
}
