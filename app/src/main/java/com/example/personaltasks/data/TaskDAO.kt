package com.example.personaltasks.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.personaltasks.model.Task

interface TaskDAO {
    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * from tasks WHERE id = :taskId")
    fun findById(taskId: Int): Task?

    @Query("SELECT * from tasks")
    fun findAll(): List<Task>
}