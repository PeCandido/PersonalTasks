# Personal Tasks
Aplicativo android desenvolvido em Kotlin com o intuito de gerenciar tarefas pessoais, permitindo criar, visualizar, editar e excluir tarefas utilizando uma interface simples e intuitiva. As tarefas criadas são persistidas localmente utilizando a biblioteca Room do Android

# Funcionalidades:
- Adicionar tarefas com título, descrição e data limite para ser feita, através do menu
- Editar tarefas criadas, podendo alterar todos os seus campos
- Remoção de tarefas criadas
- Visualização das informações da tarefa de forma detalhada
- Menu de contexto da tarefa com as opções de editar, remover e detalhar informações da tarefa
- Tarefas removidas podem ser reativadas ou permanentemente excluídas
- Persistência de dados remota via Firebase 

# Tecnologias Utilizadas:
- Linguagem Kotlin
- Android Studio (versão Ladybug)
- View Binding
- RecyclerView
- Room database

# Pré-requisitos:
- Versão mínima do Android: 8.0 (API 26 - Android Oreo)
- Android Studio versão LadyBug ou superior
- Configuração do Firebase

# Vídeo de demonstração:
https://github.com/user-attachments/assets/74653170-d941-41f4-8bda-c5d0593219c3

# Instruções para executar:
1.  Clone este repositório: "https://github.com/PeCandido/PersonalTasks.git"
2.  Abra o projeto no Android Studio (versão LadyBug ou superior)
3.  Configure o "minSDK" para API 26 Android 8.0 - Versão Oreo
4.  Sincronize o projeto com os arquivos Gradle
5.  Execute o aplicativo em um emulador de android ou com seu próprio dispositivo Android (API 26+)

# Configurando o Firebase
1. Faça login no site da Firebase (https://firebase.google.com)
2. Clique em **Go to Console**
3. Selecione o projeto PersonalTasks
4. Clique no ícone de engrenagem que fica no botão "Visão geral do projeto" e clique em **Configurações do Projeto**
5. Na aba **Geral**, desça a tela e baixe o arquivo **google-services.json**
6. Após o download, coloque esse arquivo dentro da pasta **app** do projeto



# Explicando o funcionamento do aplicativo:

## Páginas de Login e Registro:
Agora com o Firebase integrado ao projeto, é possível você criar uma conta com e-mail e senha para se registrar no aplicativo. \
Na tela de registro você pode criar uma conta no aplicativo PersonalTasks. Assim que sua conta for criada você poderá realizar login normalmente, \
mas saiba que para criar uma conta é necessário uma senha de pelo menos 6 dígitos. \
Ao logar, cada usuário terá suas próprias tarefas salvas remotamente no aplicativo.

## Página Principal
A tela principal é onde estarão listadas todas as suas tarefas. Cada tarefa possui um título, sua descrição e sua "Deadline" (que seria a data limite para ser cumprida). \
Nesta página você verá um ícone "+", esse ícone encaminha para tela de criação de uma nova tarefa. \
Caso nenhuma tarefa tenha sido criada ainda, a página principal estará vazia, contendo apenas o nome do aplicativo no cabeçalho da página. \
Quando uma nova tarefa for salva, ela aparecerá nessa tela principal com suas informações. \
Ao clicar e segurar em cima de uma tarefa na tela principal, uma janela com menu de opções da tarefa será aberto, contendo 3 opções: "Edit Task", "Remove Task" e "Details". 
- Edit Task: te encaminha para a tela de edição da tarefa, onde você poderá editar o título, a descrição e a data limite da tarefa que você selecionou;
- Remove Task: exclui a tarefa e deleta ela do banco local, a tarefa removida não será mais visível na tela principal;
- Details: te encaminha para uma tela com as informações da tarefa, mostrando todos os seus campos da tarefa de forma detalhada.

## Página de Criação de Tarefa
Após clicar no ícone de "+", você seré encaminhado para tela de criação de tarefa.
Neste campos você conseguirá colocar título, descrição e data limite da tarefa.
Caso algum campo não esteja completo, a tarefa não é criada.

## Página de Detalhes
Nessa tela são mostradas as informações da tarefa

## Página de Tarefas Removidas
Ao remover uma tarefa na tela principal, ela será encaminhada para tela de tarefas deletadas. \
Para acessar a tela de tarefas deletadas, clique no ícone de lixeiro no canto superior da tela.
Nessa tela é possível ver as tarefas que foram removidas e segurando o clique em uma tarefa, um menu de contexto surge \
para você poder reativar a tarefa para que ela volte para página principal, ver os detalhes da tarefa ou remover a tarefa permanentemente.
