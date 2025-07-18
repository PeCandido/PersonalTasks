package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class LoginActivity: AppCompatActivity() {
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var loginButton: Button
    private lateinit var goToRegisterText: TextView

    // Instância do Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Pega autenticação
        auth = FirebaseAuth.getInstance()

        // Instancia UI
        emailEdit = findViewById(R.id.edit_email_login)
        passwordEdit = findViewById(R.id.edit_password_login)
        loginButton = findViewById(R.id.login_btn)
        goToRegisterText = findViewById(R.id.go_to_register_txt)

        loginButton.setOnClickListener {
            performLogin() // faz o login do usuário
        }

        goToRegisterText.setOnClickListener {
            // Intent explícita que leva para tela de registro de conta
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin() {
        // Pega dados dos inputs de texto
        val email = emailEdit.text.toString().trim()
        val password = passwordEdit.text.toString().trim()

        // Verifica se os campos estão preenchidos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Faz autenticação com email e senha
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                // Intent explícita que leva a tela principal se o login foi um sucesso
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Exception caso o login falhe
                val errorMessage = task.exception?.message ?: "Error"
                Toast.makeText(this, "Failure to login: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }

    }

    /* Ao iniciar o aplicativo, caso o usuário estava logado
    anteriormente, o aplicativo já abre na tela principal */
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}