package com.example.conectaovinos.database.dao

import android.app.admin.TargetUser
import com.example.conectaovinos.database.entities.UsuarioEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UsuarioEntity)

    @Query("SELECT * FROM usuarios ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM  usuarios WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha LIMIT 1")
    suspend fun login(email: String, senha: String): UsuarioEntity?

}