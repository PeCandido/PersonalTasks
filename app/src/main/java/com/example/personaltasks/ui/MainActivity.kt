package com.example.personaltasks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.data.AppDatabase
import com.exemplo.personaltasks.R

class MainActivity : AppCompatActivity() {

    private val taskDao by lazy {
        AppDatabase.getDatabase(this).taskDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}