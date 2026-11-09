# Sistema de Quiz e Atividades Virtuais

Sistema gamificado de criação e aplicação de quizzes em tempo real, desenvolvido para apoiar professores na avaliação dinâmica e engajar alunos em sala de aula. Projeto referente à AEP 2026.2 do curso de Engenharia de Software.

**ODS Atendida:** ODS 4 — Educação de Qualidade.

## Requisitos Funcionais (Escopo)
* **RF01 - Criação e Regras:** O sistema deve permitir que o professor crie, edite e estruture quizzes contendo diferentes tipos de questões (múltipla escolha, verdadeiro ou falso), definindo também um gabarito, tempo limite e pontuação por pergunta.
* **RF02 - Gerenciamento de Sessão:** O sistema deve possuir uma funcionalidade para projetar o quiz na sala de aula, gerando e exibindo um código de acesso (PIN) para o login da turma.
* **RF03 - Validação e Ranking Gamificado:** O sistema deve validar as respostas em tempo real e exibir as pontuações e rankings parciais de cada pergunta para gerar engajamento e reter o interesse da turma.
* **RF04 - Painel de Resultados:** O sistema deve exibir de forma simplificada a quantidade de acertos e erros dos alunos em cada matéria, auxiliando o professor a identificar as defasagens da turma.
* **RF05 - Envio de Feedback e Dúvidas:** Ao final de cada partida, o sistema deve disponibilizar um espaço para o aluno avaliar a dinâmica e enviar dúvidas sobre a matéria para a revisão posterior do professor.

## Cronograma (Backlog Ágil)
| Sprint / Data | Épico | User Story | Responsável |
|---|---|---|---|
| Sprint 1 (Início do 2º Bi) | Gestão de Quizzes | COMO UM professor EU QUERO criar, editar e estruturar quizzes com diferentes tipos de questões, gabarito e tempo limite PARA QUE eu possa avaliar a turma. (RF01) | Felipe |
| Sprint 2 (Meio do 2º Bi) | Dinâmica de Sala | COMO UM professor EU QUERO projetar um código de acesso PARA QUE a turma faça login na partida ao vivo. (RF02) | Nicolas |
| Sprint 3 (Fim do 2º Bi) | Motor de Jogo | COMO UM aluno EU QUERO que o sistema valide minhas respostas em tempo real, mostrando pontuações e rankings PARA QUE gere mais engajamento. (RF03) | Felipe |
| Sprint 4 (Fim do 2º Bi) | Relatórios | COMO UM professor EU QUERO ver um painel simplificado de acertos e erros PARA QUE eu possa identificar e revisar as dificuldades. (RF04) | Nicolas |
| Sprint 4 (Fim do 2º Bi) | Feedbacks | COMO UM aluno EU QUERO um espaço no fim da partida para avaliar a dinâmica e enviar dúvidas PARA QUE o professor revise posteriormente. (RF05) | Ambos |

## Mudanças em relação à 1ª Entrega
O documento da 1ª entrega previa um back-end em **Spring Boot exposto como API REST**. Durante a implementação, a equipe optou por **Java puro com JDBC e arquitetura em camadas (Model, DAO, Service, View) com interface de console**, conforme já descrito na seção de tecnologias deste README. Motivos:

* **Foco nos critérios do curso (Esoft):** o objetivo da entrega é demonstrar POO (classes abstratas, herança, polimorfismo, composição) e persistência real em banco. Com JDBC as consultas SQL ficam explícitas no código, evidenciando o domínio da integração com o PostgreSQL, sem depender da "mágica" de um framework.
* **Execução simples e portável:** o projeto compila apenas com o JDK 17 e o driver JDBC, sem gerenciador de dependências, o que reduz problemas de ambiente entre Windows, Linux e macOS (disciplina de Sistemas Operacionais).
* **Escopo preservado:** todos os requisitos (RF01 a RF05), o diagrama de classes e o DER foram mantidos. A camada `view` (console) pode ser substituída futuramente por controllers REST sem alterar `model`, `dao` e `service`, justamente pela separação em camadas.
* **Limitação conhecida:** no console, a projeção do PIN e as respostas dos alunos acontecem na mesma máquina; o tempo limite das questões é exibido, mas não cronometrado.

## Equipe
* Felipe Gabriel Caspani Carlota - RA: 25113749-2
* Nicolas Henrique Dantas Da Silva - RA: 25000715-2 

## Tecnologias Utilizadas
* **Linguagem:** Java (17 LTS)
* **Banco de Dados:** PostgreSQL
* **Acesso a Dados:** JDBC
* **Padrão Arquitetural:** Camadas (Model, DAO, Service, View)
* **Controle de Versão:** Git + GitHub

## Estrutura de Diretórios
A arquitetura do projeto está organizada da seguinte forma:

```text
.
├── src/                 # Código-fonte Java, organizado em camadas
│   ├── model/           # Entidades do domínio (incluindo a hierarquia de herança)
│   ├── dao/             # Acesso a dados (isolamento da persistência)
│   ├── service/         # Regras de negócio
│   └── view/            # Interface de console
├── docs/                # PDF da entrega, diagrama de classes e DER
├── database/            # schema.sql (criação das tabelas)
├── lib/                 # driver JDBC do PostgreSQL (postgresql-42.7.14.jar)
└── README.md
```

## Como Executar Localmente

### 1. Pré-requisitos
Certifique-se de ter instalado em sua máquina:
* JDK 17 (configurado nas variáveis de ambiente `PATH`)
* PostgreSQL (instalado e em execução)
* Git

Para verificar a instalação do Java, execute no terminal:
```bash
java -version
javac -version
```

### 2. Configuração do Banco de Dados
Conecte-se ao seu PostgreSQL e crie o banco de dados:
```sql
CREATE DATABASE sistema_quiz;
```

Em seguida, execute o script de criação das tabelas contido na pasta `database/`:
```bash
psql -U seu_usuario -d sistema_quiz -f database/schema.sql
```

**Alternativa pelo pgAdmin (comum no Windows):** crie o banco `sistema_quiz` em *Databases → Create → Database*, abra o *Query Tool* desse banco, cole o conteúdo de `database/schema.sql` e execute (F5). O script deve ser executado **uma única vez**.

### 3. Driver JDBC do PostgreSQL
O driver já está na pasta `lib/` do repositório. Caso não esteja, baixe em https://jdbc.postgresql.org/download/ (`postgresql-42.7.14.jar`) e coloque o arquivo na pasta `lib/` na raiz do projeto.

### 4. Credenciais do banco
Por padrão o sistema conecta em `jdbc:postgresql://localhost:5432/sistema_quiz` com usuário `postgres` e senha `postgres`. Se o seu for diferente, defina as variáveis de ambiente antes de executar:

```bash
# Linux / macOS
export DB_USER=seu_usuario
export DB_PASSWORD=sua_senha
```
```powershell
# Windows (PowerShell)
$env:DB_USER="seu_usuario"
$env:DB_PASSWORD="sua_senha"
```
(Opcional: `DB_URL` para outro host, porta ou nome de banco.)

### 5. Compilação
Na raiz do projeto:

```bash
# Linux / macOS
javac -encoding UTF-8 -d bin -cp "lib/*" $(find src -name "*.java")
```
```powershell
# Windows (PowerShell)
javac -encoding UTF-8 -d bin -cp "lib/*" (Get-ChildItem -Recurse src -Filter *.java).FullName
```

### 6. Execução
```bash
# Linux / macOS
java -cp "bin:lib/*" view.Main
```
```powershell
# Windows (use ; no classpath)
java -cp "bin;lib/*" view.Main
```

## Funcionalidades implementadas (2ª Entrega)
Aplicação de console com todas as operações **persistidas no PostgreSQL** via JDBC.

| Requisito | Funcionalidade no menu |
|---|---|
| RF01 – Criação e regras | CRUD de quizzes e questões (múltipla escolha e V/F, com gabarito, tempo limite e pontos) |
| RF02 – Gerenciamento de sessão | Professor inicia a sessão e o sistema gera um **PIN** de 6 dígitos para a turma |
| RF03 – Validação e ranking | Aluno entra com o PIN, responde; cada resposta é corrigida, gravada e o **ranking parcial** é exibido |
| RF04 – Painel de resultados | Acertos, erros e aproveitamento por matéria dos quizzes do professor |
| RF05 – Feedback e dúvidas | Ao final da partida o aluno avalia a dinâmica e envia dúvidas ao professor |

### CRUD por entidade
| Entidade | Create | Read | Update | Delete |
|---|---|---|---|---|
| Usuário (Professor / Aluno) | ✔ | ✔ | ✔ | ✔ |
| **Quiz** (entidade principal) | ✔ | ✔ (com suas questões) | ✔ | ✔ (questões excluídas em cascata) |
| Questão | ✔ | ✔ | ✔ | ✔ |
| Sessão | ✔ (iniciar) | ✔ (listar / PIN) | ✔ (encerrar) | ✔ (com respostas e feedbacks) |
| Resposta do aluno / Feedback | ✔ | ✔ | ✔ (DAO) | ✔ (DAO) |

### Orientação a Objetos
* **Classes abstratas:** `Usuario` e `Questao`
* **Herança:** `Professor` e `Aluno` herdam de `Usuario`; `QuestaoMultiplaEscolha` e `QuestaoVerdadeiroFalso` herdam de `Questao`
* **Polimorfismo (`@Override`):** `validarResposta()` corrige cada tipo de questão de forma diferente
* **Composição 1:N:** `Quiz` → `Questao` (`ON DELETE CASCADE`); `Sessao` agrega `RespostaAluno` e `Feedback`
* **Interface:** `CrudDAO<T>`, implementada por todos os DAOs

### Camadas
* `model/` – entidades do diagrama de classes
* `dao/` – acesso ao banco com JDBC (`ConexaoBD`, `CrudDAO<T>` e um DAO por tabela)
* `service/` – regras de negócio (`UsuarioService`, `QuizService`, `SessaoService`)
* `view/` – menu de console (`MenuConsole`) e ponto de entrada (`Main`)

### Roteiro rápido de teste
1. Usuários → cadastre um **Professor** e um **Aluno**
2. Quizzes → crie um quiz (ID do professor) e adicione questões
3. Partida ao vivo → **Iniciar sessão** (anote o PIN) → **Entrar com PIN** como aluno e responda
4. Painel de resultados → veja acertos e erros por matéria
