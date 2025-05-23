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
import com.example.personaltasks.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    // Armazena o ID da tarefa, se for uma edição
    private var taskId: Int = 0
    // Indica se o formulário está em modo somente leitura (detalhamento da tarefa)
    private var isReadOnly: Boolean = false
    // Define o formato da data exibida no campo
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    // Instância de calendário usada no seletor de datas
    private val calendar = Calendar.getInstance()
    // Instância do banco de dados
    private lateinit var db: AppDatabase

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

        // Inicializa a instância do banco de dados
        db = AppDatabase.getDatabase(this)

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

            // Ainda permite exibir o DatePicker, se necessário
            dateEdit.setOnClickListener { showDatePicker() }
        }

        // Obtém o ID da tarefa passada via Intent (0 se for nova)
        taskId = intent.getIntExtra("task_id", 0)

        // Se for edição ou visualização, carrega os dados da tarefa do banco
        if (taskId != 0) {
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
        // Feedback ao usuário
        Toast.makeText(this, "Salvando tarefa...", Toast.LENGTH_SHORT).show()

        // Obtém os valores dos campos
        val title = titleEdit.text.toString()
        val description = descriptionEdit.text.toString()
        val deadline = dateEdit.text.toString()

        // Valida se todos os campos foram preenchidos
        if (title.isBlank() || description.isBlank() || deadline.isBlank()) {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Cria uma nova tarefa ou atualiza a existente
        val newTask = if (taskId != 0) {
            Task(
                id = taskId,
                title = title,
                description = description,
                deadline = deadline
            )
        } else {
            Task(
                title = title,
                description = description,
                deadline = deadline
            )
        }

        // Executa a operação de banco de dados de forma assíncrona
        lifecycleScope.launch {
            if (taskId != 0) {
                db.taskDAO().update(newTask) // Atualiza a tarefa existente
            } else {
                db.taskDAO().insert(newTask) // Insere uma nova tarefa
            }
            finish() // Fecha a activity após salvar
        }
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
        lifecycleScope.launch {
            val task = withContext(Dispatchers.IO) {
                db.taskDAO().findById(taskId)
            }

            // Se a tarefa existir, preenche os campos com seus dados
            task?.let {
                titleEdit.setText(it.title)
                descriptionEdit.setText(it.description)
                dateEdit.setText(it.deadline)
            }
        }
    }
}
