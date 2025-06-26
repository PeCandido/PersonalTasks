package com.example.personaltasks.ui

import android.os.Bundle
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
            .get()
            .addOnSuccessListener { documents ->
                val taskList = documents.toObjects(Task::class.java)
                deletedTaskAdapter.updateTasks(taskList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error to fetch tasks: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}