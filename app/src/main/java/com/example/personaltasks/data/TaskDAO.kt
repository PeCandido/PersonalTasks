package com.example.personaltasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.personaltasks.model.Task

// DAO da tarefa para realizar persistência no banco de dados com Room
@Dao
interface TaskDAO {
    // Função para criar uma nova tarefa
    @Insert
    suspend fun insert(task: Task)

    // Função para editar uma tarefa existente
    @Update
    suspend fun update(task: Task)

    // Função para remover uma tarefa existente
    @Delete
    suspend fun delete(task: Task)

    // Função para encontrar uma tarefa específica utilizando query sql do Room
    @Query("SELECT * from tasks WHERE id = :taskId")
    suspend fun findById(taskId: Int): Task?

    // Função para puxar todas as tarefas criadas utiliazando query sql do Room
    @Query("SELECT * from tasks")
    suspend fun findAll(): List<Task>
}