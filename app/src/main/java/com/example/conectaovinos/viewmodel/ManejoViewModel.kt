package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.entities.ManejoEntity
import com.example.conectaovinos.database.repository.ManejoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ManejoViewModel(private val repository: ManejoRepository) : ViewModel() {

    val manejos = repository.manejos

    fun getManejosByAnimal(animalId: Int?): Flow<List<ManejoEntity>> {
        return repository.getManejosByAnimal(animalId)
    }


    fun addManejo(animalId: Int?, tipo: String, observacao: String, data: String) {
        viewModelScope.launch {
            repository.insertManejo(
                ManejoEntity(
                    animalId = animalId,
                    tipoManejo = tipo,
                    observacao = observacao,
                    data = data
                )
            )
        }
    }

}
