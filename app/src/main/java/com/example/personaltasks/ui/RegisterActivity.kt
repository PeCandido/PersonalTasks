package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity: AppCompatActivity() {

    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var registerButton: Button

    // Instância do Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Instância da autenticação
        auth = FirebaseAuth.getInstance()

        emailEdit = findViewById(R.id.edit_email)
        passwordEdit = findViewById(R.id.edit_password)
        registerButton = findViewById(R.id.register_btn)

        registerButton.setOnClickListener{
            // Faz o registro da conta
            performRegistration()
        }
    }

    private fun performRegistration() {
        val email = emailEdit.text.toString().trim()
        val password = passwordEdit.text.toString().trim()

        // Verifica se os campos estão completos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Cria usuário com email e senha
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "Account Registered!", Toast.LENGTH_SHORT).show()

                    // Desloga o usuário para mandá-lo para tela de login
                    auth.signOut()

                    // Intent explícita que leva a tela de login
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // Exception caso de erro de registro
                    val errorMessage = task.exception?.message ?: "Error"
                    Toast.makeText(this, "Failure to register: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }
}