package com.example.personaltasks.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.personaltasks.data.AppDatabase
import com.exemplo.personaltasks.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskFormActivity : AppCompatActivity() {
    private lateinit var titleEdit: EditText
    private lateinit var descriptionEdit: EditText
    private lateinit var dateEdit: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var taskId: Int = 0
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        titleEdit = findViewById(R.id.edit_title)
        descriptionEdit = findViewById(R.id.edit_description)
        dateEdit = findViewById(R.id.edit_limit_date)
        saveButton = findViewById(R.id.clear_btn)
        cancelButton = findViewById(R.id.save_btn)

        db = AppDatabase.getDatabase(this)

        taskId = intent.getIntExtra("task_id", 0)

        if (taskId != 0) {
            loadTask()
        }
    }

    private fun loadTask() {
        lifecycleScope.launch {
            val task = withContext(Dispatchers.IO) {
                db.taskDAO().findById(taskId)
            }

            task?.let {
                titleEdit.setText(it.title)
                descriptionEdit.setText(it.description)
                dateEdit.setText(it.deadline)
            }
        }
    }
}