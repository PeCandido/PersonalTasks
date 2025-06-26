package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskAdapter
import com.example.personaltasks.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DeletedTasksActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var deletedTaskAdapter: TaskAdapter
    private var selectedTask: Task? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val tasksCollection = firestore.collection("tasks")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deleted_tasks)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_deleted)
        setSupportActionBar(toolbar)

        val closeButton = findViewById<ImageButton>(R.id.close_button)

        // Defina o que acontece ao clicar nele
        closeButton.setOnClickListener {
            finish() // Fecha a activity
        }

        setupRecyclerView()
        fetchDeletedTasks()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rv_deleted_tasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        deletedTaskAdapter = TaskAdapter(mutableListOf()) { view, task ->
            selectedTask = task
            view.showContextMenu()
        }
        recyclerView.adapter = deletedTaskAdapter

        registerForContextMenu(recyclerView)
    }

    private fun fetchDeletedTasks() {
        val userId = auth.currentUser?.uid ?: return

        tasksCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("deleted", true)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(this, "Error to fetch tasks: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val taskList = snapshots.toObjects(Task::class.java)
                    deletedTaskAdapter.updateTasks(taskList)
                }
            }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.deleted_task_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_reactivate_task -> {
                selectedTask?.let { reactivateTask(it) }
                true
            }
            R.id.menu_details -> {
                selectedTask?.let { showTaskDetails(it) }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun reactivateTask(task: Task) {
        task.id?.let { taskId ->
            tasksCollection.document(taskId)
                .update("deleted", false)
                .addOnSuccessListener {
                    Toast.makeText(this, "Task got reactivated!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showTaskDetails(task: Task) {
        val intent = Intent(this, TaskFormActivity::class.java).apply {
            putExtra("task_id", task.id)
            putExtra("read_only", true)
        }
        startActivity(intent)
    }
}