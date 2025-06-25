package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class LoginActivity: AppCompatActivity() {
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var loginButton: Button
    private lateinit var goToRegisterText: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailEdit = findViewById(R.id.edit_email_login)
        passwordEdit = findViewById(R.id.edit_password_login)
        loginButton = findViewById(R.id.login_btn)
        goToRegisterText = findViewById(R.id.go_to_register_txt)

        loginButton.setOnClickListener {
            performLogin()
        }

        goToRegisterText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin() {

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}