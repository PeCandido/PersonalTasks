package com.example.personaltasks.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabela no banco de dados que armazena as tarefas
@Entity(tableName = "tasks")
class Task (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val deadline: String,
    val isDone: Boolean
)