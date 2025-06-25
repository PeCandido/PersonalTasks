package com.example.personaltasks.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

// Tabela no banco de dados que armazena as tarefas
class Task (
    @DocumentId
    var id: Int = 0,
    var userId: String? = null,
    var title: String = "",
    var description: String = "",
    var deadline: String = "",
    var isDone: Boolean = false,
    var isDeleted: Boolean = false
)