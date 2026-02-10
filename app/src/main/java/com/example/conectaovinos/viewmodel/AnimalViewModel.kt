package com.example.conectaovinos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.AppDatabase
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.repository.AnimalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AnimalViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: AnimalRepository
    val animals : Flow<List<AnimalEntity>>


    init {
        val dao = AppDatabase.getDatabase(application).animalDao()
        repo = AnimalRepository(dao)
        animals = repo.animals
    }

    fun AddAnimal(nome: String, raca: String, idade: Int, preco: Double){
        viewModelScope.launch {
            repo.insert(AnimalEntity(nome = nome, raca = raca, idade = idade, preco = preco))
        }
    }

}