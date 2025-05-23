# Personal Tasks
Aplicativo android desenvolvido em Kotlin com o intuito de gerenciar tarefas pessoais, permitindo criar, visualizar, editar e excluir tarefas utilizando uma interface simples e intuitiva. As tarefas criadas são persistidas localmente utilizando a biblioteca Room do Android

# Funcionalidades:
- Adicionar tarefas com título, descrição e data limite para ser feita, através do menu
- Editar tarefas criadas, podendo alterar todos os seus campos
- Remoção de tarefas criadas
- Visualização das informações da tarefa de forma detalhada
- Menu de contexto da tarefa com as opções de editar, remover e detalhar informações da tarefa 
- Salvamento local utilizando biblioteca Room

# Tecnologias Utilizadas:
- Linguagem Kotlin
- Android Studio (versão Ladybug)
- View Binding
- RecyclerView
- Room database

# Pré-requisitos:
- Versão mínima do Android: 8.0 (API 26 - Android Oreo)
- Android Studio versão LadyBug ou superior

# Vídeo de demonstração:
https://github.com/user-attachments/assets/18bafedb-b1be-4b65-8284-cb50ead66163

# Instruções para executar:
1.  Clone este repositório: "https://github.com/PeCandido/PersonalTasks.git"
2.  Abra o projeto no Android Studio (versão LadyBug ou superior)
3.  Configure o "minSDK" para API 26 Android 8.0 - Versão Oreo
4.  Sincronize o projeto com os arquivos Gradle
5.  Execute o aplicativo em um emulador de android ou com seu próprio dispositivo Android (API 26+)

# Explicando o funcionamento:

## Página Principal
A tela principal é onde estarão listadas todas as suas tarefas. Cada tarefa possui um título, sua descrição e sua "Deadline" (que seria a data limite para ser cumprida). \
Nesta página você verá um ícone "+", esse ícone encaminha para tela de criação de uma nova tarefa. \
Caso nenhuma tarefa tenha sido criada ainda, a página principal estará vazia, contendo apenas o nome do aplicativo no cabeçalho da página. \
Quando uma nova tarefa for salva, ela aparecerá nessa tela principal com suas informações. \
Ao clicar e segurar em cima de uma tarefa na tela principal, uma janela com menu de opções da tarefa será aberto, contendo 3 opções: "Edit Task", "Remove Task" e "Details". \
- Edit Task: te encaminha para a tela de edição da tarefa, onde você poderá editar o título, a descrição e a data limite da tarefa que você selecionou
- Remove Task: exclui a tarefa e deleta ela do banco local, a tarefa removida não será mais visível na tela principal
- Details: te encaminha para uma tela com as informações da tarefa, mostrando todos os seus campos da tarefa de forma detalhada

## Página de Criação de Tarefa
Após clicar no ícone de "+", você seré encaminhado para tela de criação de tarefa
Neste campos você conseguirá colocar título, descrição e data limite da tarefa
Caso algum campo não esteja completo, a tarefa não é criada



