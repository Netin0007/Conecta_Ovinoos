package com.example.conectaovinos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectaovinos.database.entities.UsuarioEntity
import com.example.conectaovinos.database.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // Lista de usuários observável
    val usuarios = repository.users

    // Inserir usuário
    fun addUser(usuario: UsuarioEntity) {
        viewModelScope.launch {
            repository.insertUser(usuario)
        }
    }

    // Buscar usuário por email
    suspend fun getUserByEmail(email: String): UsuarioEntity? {
        return repository.getUserByEmail(email)
    }
}
