# Sistema de Quiz e Atividades Virtuais 

Sistema gamificado de criação e aplicação de quizzes em tempo real, desenvolvido para apoiar professores na avaliação dinâmica e engajar alunos em sala de aula. Projeto referente à AEP 2026.2 do curso de Engenharia de Software.

**ODS Atendida:** ODS 4 — Educação de Qualidade.

## Equipe
* Felipe Gabriel Caspani Carlota- RA: 25113749-2
* Nicolas Henrique Dantas da Silva - RA: 25000715-2

## Tecnologias Utilizadas
* **Linguagem:** Java (17 LTS)
* **Banco de Dados:** PostgreSQL
* **Acesso a Dados:** JDBC
* **Padrão Arquitetural:** Camadas (Model, DAO, Service, View)
* **Controle de Versão:** Git + GitHub

## Estrutura de Diretórios
A arquitetura do projeto está organizada da seguinte forma:

.
├── src/                 # Código-fonte Java, organizado em camadas
│   ├── model/           # Entidades do domínio (incluindo a hierarquia de herança)
│   ├── dao/             # Acesso a dados (isolamento da persistência)
│   ├── service/         # Regras de negócio
│   └── view/            # Interface de console
├── docs/                # PDF da entrega, diagrama de classes e DER
├── database/            # schema.sql (criação das tabelas)
└── README.md


## Como Executar Localmente

### 1. Pré-requisitos
Certifique-se de ter instalado em sua máquina:
* JDK 17 (configurado nas variáveis de ambiente `PATH`)
* PostgreSQL (instalado e em execução)
* Git

Para verificar a instalação do Java, execute no terminal:
java -version
javac -version


### 2. Configuração do Banco de Dados
Conecte-se ao seu PostgreSQL e crie o banco de dados:
CREATE DATABASE sistema_quiz;


Em seguida, execute o script de criação das tabelas contido na pasta `database/`:
psql -U seu_usuario -d sistema_quiz -f database/schema.sql


### 3. Compilação
No terminal, a partir da raiz do projeto, execute o comando abaixo para compilar as classes Java:
javac -d bin -cp caminho/para/driver-postgresql.jar $(find src -name "*.java")

*(Nota: No Windows, utilize `;` no lugar de `:` como separador de classpath e adapte o separador de diretórios se necessário).*

### 4. Execução
Para iniciar o sistema via console, utilize o comando:
java -cp bin:caminho/para/driver-postgresql.jar pacote.ClassePrincipal
