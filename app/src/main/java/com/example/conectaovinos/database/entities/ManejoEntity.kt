package com.example.conectaovinos.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.conectaovinos.database.enums.tipoManejo



@Entity(tableName = "manejoTable")
data class ManejoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val animalId: Int,
    val tipo: tipoManejo,
    val descricao: String,
    val data: String
)