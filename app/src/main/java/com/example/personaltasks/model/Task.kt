package com.example.personaltasks.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

data class Task(
    // Tag Firebase que indica atributo ID da classe
    @DocumentId
    var id: String? = null,
    var userId: String? = null,
    var title: String = "",
    var description: String = "",
    var deadline: String = "",
    var isDone: Boolean = false,
    var isDeleted: Boolean = false
) {
    constructor() : this(null, null, "", "", "", false, false)
}