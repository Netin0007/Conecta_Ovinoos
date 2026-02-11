package com.example.conectaovinos.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "manejos",
    foreignKeys = [
        ForeignKey(
            entity = AnimalEntity::class,
            parentColumns = ["id"],
            childColumns = ["animalId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ManejoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val animalId: Int?,
    val tipoManejo: String,
    val data: String,
    val observacao: String
)

