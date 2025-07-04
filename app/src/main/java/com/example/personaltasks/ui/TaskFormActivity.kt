package com.example.personaltasks.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.model.Task
import com.example.personaltasks.R
import com.example.personaltasks.enum.Priority
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Activity responsável por exibir o formulário de criação, edição ou visualização de uma tarefa
class TaskFormActivity : AppCompatActivity() {

    // Declaração dos elementos da interface
    private lateinit var titleEdit: EditText
    private lateinit var descriptionEdit: EditText
    private lateinit var dateEdit: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var isDoneCB: CheckBox
    private lateinit var prioritySpinner: Spinner

    // Armazena o ID da tarefa, se for uma edição
    private var taskId: String? = null
    // Indica se o formulário está em modo somente leitura (detalhamento da tarefa)
    private var isReadOnly: Boolean = false
    // Define o formato da data exibida no campo
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    // Instância de calendário usada no seletor de datas
    private val calendar = Calendar.getInstance()

    // Instância do Firebase
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val tasksCollection = firestore.collection("tasks")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout da tela
        setContentView(R.layout.activity_task)

        // Inicializa os componentes da UI com base nos IDs do layout
        titleEdit = findViewById(R.id.edit_title)
        descriptionEdit = findViewById(R.id.edit_description)
        dateEdit = findViewById(R.id.edit_deadline)
        saveButton = findViewById(R.id.save_btn)
        cancelButton = findViewById(R.id.cancel_btn)
        isDoneCB = findViewById(R.id.isDone_checkbox)
        prioritySpinner = findViewById(R.id.priority_spinner)

        // Desabilita o teclado na edição da data
        dateEdit.setKeyListener(null)

        // Recupera se a tela deve ser exibida como somente leitura (detalhamento da tarefa)
        isReadOnly = intent.getBooleanExtra("read_only", false)

        // Se for somente leitura, desativa os campos de entrada e esconde o botão de salvar
        if (isReadOnly) {
            saveButton.visibility = View.GONE
            cancelButton.text = "Cancel" // Altera o texto do botão de "Cancelar" para "Cancel"

            // Desabilita a edição dos campos
            titleEdit.isEnabled = false
            descriptionEdit.isEnabled = false
            dateEdit.isEnabled = false
            isDoneCB.isEnabled = false

            // Ainda permite exibir o DatePicker, se necessário
            dateEdit.setOnClickListener { showDatePicker() }
        }

        // Obtém o ID da tarefa passada via Intent (0 se for nova)
        taskId = intent.getStringExtra("task_id")

        // Se for edição ou visualização, carrega os dados da tarefa do banco
        if (taskId != null) {
            loadTask()
        }

        // Ao clicar no campo de data, mostra o seletor de datas
        dateEdit.setOnClickListener {
            showDatePicker()
        }

        // Ao clicar no botão de salvar, tenta salvar ou atualizar a tarefa
        saveButton.setOnClickListener {
            saveTask()
        }

        // Ao clicar no botão de cancelar, fecha a tela
        cancelButton.setOnClickListener {
            finish()
        }
    }

    // Função responsável por salvar ou atualizar a tarefa
    private fun saveTask() {
        // Obtém os valores dos campos
        val title = titleEdit.text.toString().trim()
        val description = descriptionEdit.text.toString().trim()
        val deadline = dateEdit.text.toString()
        val isTaskDone = isDoneCB.isChecked
        val priority = getPriority(prioritySpinner.selectedItem.toString())

        // Verifica se tem um usuário logado
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Error: No users logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        // Valida se todos os campos foram preenchidos
        if (title.isBlank() || description.isBlank() || deadline.isBlank()) {
            // Feedback ao usuário avisando que todos os campos devem ser preenchidos
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Feedback ao usuário avisando que a tarefa foi salva
        Toast.makeText(this, "Saving task...", Toast.LENGTH_SHORT).show()

        // Cria uma nova tarefa ou atualiza a existente
        val taskToSave = Task(
            id = taskId, // Será nulo se for uma nova tarefa
            userId = userId,
            title = title,
            description = description,
            deadline = deadline,
            isDone = isTaskDone,
            isDeleted = false,
            priority = priority
        )

        // Cria uma nova tarefa quando o ID é nulo
        if (taskId == null) {
            tasksCollection.add(taskToSave)
                .addOnSuccessListener {
                    Toast.makeText(this, "Created new task!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Faz atualização da tarefa
            tasksCollection.document(taskId!!)
                .set(taskToSave)
                .addOnSuccessListener {
                    Toast.makeText(this, "Task has been updated!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Encerra activity
        finish()
    }

    private fun getPriority(priority: String): Priority {
        if(priority == "Medium") return Priority.MEDIUM
        if(priority == "High") return Priority.HIGH
        return Priority.LOW
    }

    // Exibe o seletor de data (DatePicker)
    private fun showDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            // Atualiza o calendário com a data selecionada
            calendar.set(year, month, dayOfMonth)
            // Mostra a data formatada no campo
            dateEdit.setText(dateFormat.format(calendar.time))
        }

        // Cria e exibe o diálogo do seletor de data
        val dialog = DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Impede que o usuário selecione uma data no passado
        dialog.datePicker.minDate = System.currentTimeMillis()
        dialog.show()
    }

    // Carrega a tarefa do banco com base no ID recebido
    private fun loadTask() {
        taskId?.let { id ->
            tasksCollection.document(id).get().addOnSuccessListener { document ->
                val task = document.toObject(Task::class.java)
                task?.let {
                    titleEdit.setText(it.title)
                    descriptionEdit.setText(it.description)
                    dateEdit.setText(it.deadline)
                    isDoneCB.isChecked = it.isDone
                }
            }
        }
    }
}
