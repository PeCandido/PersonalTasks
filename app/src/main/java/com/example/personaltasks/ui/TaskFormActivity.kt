package com.example.personaltasks.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.data.AppDatabase
import com.exemplo.personaltasks.R

class TaskFormActivity : AppCompatActivity() {
    private lateinit var titleEdit: EditText
    private lateinit var descriptionEdit: EditText
    private lateinit var dateEdit: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var taskId: Long = 0
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

        taskId = intent.getLongExtra("task_id", 0)
    }
}