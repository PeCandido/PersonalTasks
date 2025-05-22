package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
    private var selectedTask: Task? = null

    private val taskDao by lazy {
        AppDatabase.getDatabase(this).taskDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_taks)

        registerForContextMenu(recyclerView)

        db = AppDatabase.getDatabase(this)

        taskAdapter = TaskAdapter(tasks) { view, task ->
            selectedTask = task
            view.showContextMenu()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_task -> {
                openFormActivity(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openFormActivity(task: Task?) {
        val intent = Intent(this, TaskFormActivity::class.java)
        if (task != null) {
            intent.putExtra("task_id", task.id)
        }
        startActivity(intent)
    }

    private fun deleteTask(task: Task) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.taskDAO().delete(task)
            }

            loadTasks()
        }
    }


}