package com.example.personaltasks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.adapter.TaskAdapter
import com.example.personaltasks.data.AppDatabase
import com.example.personaltasks.model.Task
import com.exemplo.personaltasks.R

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
    }
}