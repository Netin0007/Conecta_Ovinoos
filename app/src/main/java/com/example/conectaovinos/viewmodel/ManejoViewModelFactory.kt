package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.conectaovinos.database.repository.ManejoRepository

class ManejoViewModelFactory(
    private val repository: ManejoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManejoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManejoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
