-- Tabela usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    tipo_usuario VARCHAR(50) NOT NULL
);

-- Tabela quizzes
CREATE TABLE quizzes (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    disciplina VARCHAR(255) NOT NULL,
    professor_id INT NOT NULL,
    CONSTRAINT fk_professor FOREIGN KEY (professor_id) REFERENCES usuarios(id)
);

-- Tabela questoes
CREATE TABLE questoes (
    id SERIAL PRIMARY KEY,
    quiz_id INT NOT NULL,
    enunciado VARCHAR(255) NOT NULL,
    tipo_questao VARCHAR(50) NOT NULL,
    gabarito VARCHAR(255) NOT NULL,
    tempo_limite_seg INT NOT NULL,
    pontos INT NOT NULL,
    CONSTRAINT fk_quiz_questoes FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Tabela sessoes
CREATE TABLE sessoes (
    id SERIAL PRIMARY KEY,
    quiz_id INT NOT NULL,
    pin_acesso INT NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_quiz_sessoes FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Tabela respostas_alunos
CREATE TABLE respostas_alunos (
    id SERIAL PRIMARY KEY,
    aluno_id INT NOT NULL,
    sessao_id INT NOT NULL,
    questao_id INT NOT NULL,
    resposta_submetida VARCHAR(255) NOT NULL,
    pontos_obtidos INT NOT NULL,
    data_resposta TIMESTAMP NOT NULL,
    CONSTRAINT fk_aluno_respostas FOREIGN KEY (aluno_id) REFERENCES usuarios(id),
    CONSTRAINT fk_sessao_respostas FOREIGN KEY (sessao_id) REFERENCES sessoes(id),
    CONSTRAINT fk_questao_respostas FOREIGN KEY (questao_id) REFERENCES questoes(id)
);

-- Tabela feedbacks
CREATE TABLE feedbacks (
    id SERIAL PRIMARY KEY,
    aluno_id INT NOT NULL,
    sessao_id INT NOT NULL,
    opiniao_dinamica VARCHAR(200),
    duvida_conteudo TEXT,
    CONSTRAINT fk_aluno_feedbacks FOREIGN KEY (aluno_id) REFERENCES usuarios(id),
    CONSTRAINT fk_sessao_feedbacks FOREIGN KEY (sessao_id) REFERENCES sessoes(id)
);