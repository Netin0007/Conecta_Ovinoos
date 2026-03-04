package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.repository.AnimalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    repository: AnimalRepository
) : ViewModel() {

    val animais: StateFlow<List<AnimalEntity>> =
        repository.getAllAnimals()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}