package com.example.personaltasks.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.personaltasks.data.AppDatabase
import com.example.personaltasks.model.Task
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
    private var isReadOnly: Boolean = false
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        isReadOnly = intent.getBooleanExtra("read_only", false)

        if (isReadOnly) {
            saveButton.visibility = View.GONE
            cancelButton.text = "Voltar"

            titleEdit.isEnabled = false
            descriptionEdit.isEnabled = false
            dateEdit.isEnabled = false

            dateEdit.setOnClickListener { showDatePicker() }
        }

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

        dateEdit.setOnClickListener {
            showDatePicker()
        }

        saveButton.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = titleEdit.text.toString()
        val description = descriptionEdit.text.toString()
        val deadline = dateEdit.text.toString()

        if (title.isBlank() || description.isBlank() || deadline.isBlank()) {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val newTask = Task(
            id = if (taskId != 0) taskId else 0,
            title = title,
            description = description,
            deadline = deadline
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (taskId != 0) {
                    db.taskDAO().update(newTask)
                } else {
                    db.taskDAO().insert(newTask)
                }
            }
            finish()
        }
    }

    private fun showDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            dateEdit.setText(dateFormat.format(calendar.time))
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
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