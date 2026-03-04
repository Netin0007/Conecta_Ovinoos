package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.dao.AnimalDao
import com.example.conectaovinos.database.entities.AnimalEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AnimalDetailsViewModel(
    animalDao: AnimalDao,
    animalId: Int
) : ViewModel() {

    val animal: StateFlow<AnimalEntity?> =
        animalDao.getById(animalId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

class AnimalDetailsViewModelFactory(
    private val animalDao: AnimalDao,
    private val animalId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnimalDetailsViewModel(animalDao, animalId) as T
    }
}