package com.example.personaltasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.personaltasks.model.Task

@Dao
interface TaskDAO {
    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * from tasks WHERE id = :taskId")
    suspend fun findById(taskId: Int): Task?

    @Query("SELECT * from tasks")
    suspend fun findAll(): List<Task>
}