# Sistema de Quiz e Atividades Virtuais

Sistema gamificado de criação e aplicação de quizzes em tempo real, desenvolvido para apoiar professores na avaliação dinâmica e engajar alunos em sala de aula. Projeto referente à AEP 2026.2 do curso de Engenharia de Software.

**ODS Atendida:** ODS 4 — Educação de Qualidade.

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

### 3. Driver JDBC do PostgreSQL
Baixe o driver em https://jdbc.postgresql.org/download/ (ex.: `postgresql-42.7.4.jar`) e coloque o arquivo na pasta `lib/` na raiz do projeto.

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
Menu de console com operações completas de **CRUD persistidas no PostgreSQL** via JDBC:

| Entidade | Create | Read | Update | Delete |
|---|---|---|---|---|
| Usuário (Professor / Aluno) | ✔ | ✔ | ✔ | ✔ |
| **Quiz** (entidade principal – RF01) | ✔ | ✔ (com suas questões) | ✔ | ✔ (exclui as questões em cascata) |
| Questão (Múltipla escolha / V ou F) | ✔ | ✔ | ✔ | ✔ |

Também é possível **jogar um quiz salvo no banco**: as respostas são corrigidas por `validarResposta()`, sobrescrito em cada tipo de questão (polimorfismo).

### Camadas
* `model/` – entidades (`Usuario` → `Professor`/`Aluno`, `Questao` → `QuestaoMultiplaEscolha`/`QuestaoVerdadeiroFalso`, `Quiz` com composição 1:N de `Questao`)
* `dao/` – acesso ao banco com JDBC (`ConexaoBD`, interface `CrudDAO<T>` e um DAO por tabela)
* `service/` – regras de negócio e validações (ex.: só professor cria quiz; gabarito V/F aceita apenas V ou F)
* `view/` – menu de console (`MenuConsole`) e ponto de entrada (`Main`)
