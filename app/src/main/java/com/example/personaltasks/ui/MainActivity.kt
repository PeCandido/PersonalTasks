package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.adapter.TaskAdapter
import com.example.personaltasks.data.AppDatabase
import com.example.personaltasks.model.Task
import com.example.personaltasks.R
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

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.rv_tasks)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
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
            R.id.new_task -> {
                openFormActivity(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openFormActivity(task: Task? = null, isReadOnly: Boolean = false) {
        val intent = Intent(this, TaskFormActivity::class.java)

        task?.let {
            intent.putExtra("task_id", it.id)
        }

        intent.putExtra("read_only", isReadOnly)
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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_task -> {
                selectedTask?.let { openFormActivity(it) }
                true
            }

            R.id.remove_task -> {
                selectedTask?.let { deleteTask(it) }
                true
            }

            R.id.detail_task -> {
                selectedTask?.let { openFormActivity(it, isReadOnly = true) }
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }
}