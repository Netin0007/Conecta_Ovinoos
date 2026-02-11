package com.example.conectaovinos.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.conectaovinos.database.AppDatabase
import com.example.conectaovinos.database.repository.AnimalRepository
import com.example.conectaovinos.database.repository.ManejoRepository
import com.example.conectaovinos.database.repository.TransacaoRepository
import com.example.conectaovinos.database.repository.UsuarioRepository

class AppViewModelFactory(
    private val animalRepo: AnimalRepository,
    private val usuarioRepo: UsuarioRepository,
    private val transacaoRepo: TransacaoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AnimalViewModel::class.java) ->
                AnimalViewModel(animalRepo) as T

            modelClass.isAssignableFrom(UsuarioViewModel::class.java) ->
                UsuarioViewModel(usuarioRepo) as T

            modelClass.isAssignableFrom(TransacaoViewModel::class.java) ->
                TransacaoViewModel(transacaoRepo) as T

            else -> throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}
