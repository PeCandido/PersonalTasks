package com.example.personaltasks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.adapter.TaskAdapter
import com.example.personaltasks.data.AppDatabase
import com.example.personaltasks.model.Task
import com.exemplo.personaltasks.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var db: AppDatabase
    private var tasks: List<Task> = listOf()

    private val taskDao by lazy {
        AppDatabase.getDatabase(this).taskDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_taks)

        db = AppDatabase.getDatabase(this)

        taskAdapter = TaskAdapter(tasks) {
            view, task ->
        }

        recyclerView.adapter = taskAdapter

        loadTasks()
    }

    private fun loadTasks() {
        lifecycleScope.launch {
            val taskList = withContext(Dispatchers.IO) {
                db.taskDAO().findAll()
            }

            tasks = taskList
            taskAdapter.updateTasks(tasks)
        }
    }
}