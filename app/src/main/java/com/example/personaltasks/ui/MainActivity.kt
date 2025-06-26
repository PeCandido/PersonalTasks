package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.adapter.TaskAdapter
import com.example.personaltasks.model.Task
import com.example.personaltasks.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Activity principal do app, onde é exibida a lista de tarefas
class MainActivity : AppCompatActivity() {

    // Componentes da interface
    private lateinit var recyclerView: RecyclerView      // Lista de tarefas
    private lateinit var taskAdapter: TaskAdapter        // Adaptador para exibir tarefas no RecyclerView
    private var tasks: List<Task> = listOf()             // Lista de tarefas carregadas
    private var selectedTask: Task? = null               // Tarefa selecionada (para o menu de contexto)

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val tasksCollection = firestore.collection("tasks")

    // Função executada quando a Activity é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Define o layout da tela

        // Configura a toolbar personalizada
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Inicializa o RecyclerView e define seu layout como uma lista vertical
        recyclerView = findViewById(R.id.rv_tasks)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        // Habilita o menu de contexto (ao segurar sobre uma tarefa)
        registerForContextMenu(recyclerView)

        // Inicializa o adaptador da lista, com callback para clique longo (context menu)
        taskAdapter = TaskAdapter(tasks) { view, task ->
            selectedTask = task         // Armazena a tarefa selecionada
            view.showContextMenu()      // Exibe o menu de contexto
        }

        // Conecta o adaptador ao RecyclerView
        recyclerView.adapter = taskAdapter

        // Carrega as tarefas do banco e exibe na lista
        loadTasks()
    }

    // Carrega todas as tarefas do banco e atualiza o RecyclerView
    private fun loadTasks() {
        val userId = auth.currentUser?.uid ?: return

        tasksCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("deleted", false)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val taskList = snapshots.toObjects(Task::class.java)
                    taskAdapter.updateTasks(taskList)
                }
            }
    }

    // Infla o menu superior (toolbar)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Função que trata os cliques nos itens do menu da toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_task -> {
                openFormActivity(null) // Abre formulário para nova tarefa
                true
            }

            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Abre o formulário de tarefa (para criação, edição ou detalhamento)
    private fun openFormActivity(task: Task? = null, isReadOnly: Boolean = false) {
        val intent = Intent(this, TaskFormActivity::class.java)

        // Passa o ID da tarefa se for edição ou visualização
        task?.let {
            intent.putExtra("task_id", it.id)
        }

        // Passa se o formulário será somente leitura (detalhamento da tarefa)
        intent.putExtra("read_only", isReadOnly)
        startActivity(intent) // Inicia a TaskFormActivity
    }

    // Remove a tarefa do banco de dados
    private fun deleteTask(task: Task) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.taskDAO().delete(task) // Deleta no banco
            }

            loadTasks() // Atualiza a lista na tela
        }
    }

    // Trata os itens selecionados no menu de contexto (ao segurar sobre uma tarefa)
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_task -> {
                selectedTask?.let { openFormActivity(it) } // Abre formulário para edição
                true
            }

            R.id.remove_task -> {
                selectedTask?.let { deleteTask(it) }    // Remove a tarefa
                true
            }

            R.id.detail_task -> {
                selectedTask?.let { openFormActivity(it, isReadOnly = true) } // Abre só para visualizar
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    // Cria o menu de contexto ao manter o dedo pressionado sobre uma tarefa
    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu) // Infla o menu com opções de editar/remover/ver
    }
}
