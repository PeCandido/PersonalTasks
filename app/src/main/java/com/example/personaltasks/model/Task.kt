package com.example.personaltasks.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val deadline: String
)
