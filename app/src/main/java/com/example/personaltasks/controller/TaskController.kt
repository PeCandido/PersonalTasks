package com.example.personaltasks.controller

import com.example.personaltasks.data.TaskDAO
import com.example.personaltasks.data.TaskSqlite
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.MainActivity

class TaskController (mainActivity: MainActivity) {
    private val taskDAO: TaskDAO = TaskSqlite(mainActivity)

    fun insert(task: Task) = taskDAO.insert(task)
    fun update(task: Task) = taskDAO.update(task)
    fun delete(task: Task) = taskDAO.delete(task)
    fun getTask(id: Int) = taskDAO.findById(id)
    fun getAllTasks() = taskDAO.findAll()
}