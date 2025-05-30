package com.example.personaltasks.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.model.Task
import com.example.personaltasks.databinding.TaskItemBinding

// Adapter responsável por exibir a lista de tarefas no RecyclerView
class TaskAdapter(
    private var tasks: List<Task>, // Lista de tarefas que será exibida
    private val onLongClick: (View, Task) -> Unit // Callback executado ao clicar e segurar em uma tarefa
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ViewHolder que representa cada item da lista
    inner class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Associa os dados da tarefa às views do layout
        fun bind(task: Task) {
            binding.taskTitle.text = task.title // Define o título da tarefa
            binding.taskDescription.text = task.description // Define a descrição da tarefa
            binding.taskDate.text = "Deadline: ${task.deadline}" // Define a data limite da tarefa
            binding.isDone.isChecked = task.isDone
            binding.isDone.isClickable = false

            // Define o que acontece ao clicar e segurar uma tarefa
            binding.root.setOnLongClickListener {
                onLongClick(it, task) // Executa o callback fornecido
                true // Retorna true para indicar que o evento foi consumido
            }
        }
    }

    // Cria um novo ViewHolder quando necessário
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(
            LayoutInflater.from(parent.context), // Cria um inflater com o contexto do pai
            parent, // ViewGroup pai (RecyclerView)
            false // Não anexa imediatamente à raiz
        )
        return TaskViewHolder(binding) // Retorna o ViewHolder criado
    }

    // Associa os dados da tarefa ao ViewHolder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position]) // Passa a tarefa atual para o metodo bind
    }

    // Retorna a quantidade de itens na lista
    override fun getItemCount(): Int = tasks.size

    // Atualiza a lista de tarefas e notifica o RecyclerView para redesenhar os itens
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks // Substitui a lista antiga pela nova
        notifyDataSetChanged() // Notifica o RecyclerView que os dados foram alterados
    }
}
