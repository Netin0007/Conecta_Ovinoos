package com.example.conectaovinos.database.repository

import com.example.conectaovinos.database.dao.UsuarioDao
import com.example.conectaovinos.database.entities.UsuarioEntity

class UsuarioRepository(private val dao: UsuarioDao) {

    val users = dao.getAllUsers()

    suspend fun insertUser(user: UsuarioEntity) {
        dao.addUser(user)
    }

    suspend fun getUserByEmail(email: String): UsuarioEntity? {
        return dao.getUserByEmail(email)
    }

    suspend fun login(email: String, senha: String): UsuarioEntity? {
        return dao.login(email, senha)
    }
}
