CREATE TABLE usuarios(
    id INT PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100),
    tipo_usuario VARCHAR(100)
);

CREATE TABLE quizzes(
    id INT PRIMARY KEY,
    titulo VARCHAR(100),
    disciplina VARCHAR(100),
    professor_id INT
);

CREATE TABLE sessoes(
    id INT PRIMARY KEY,
    quiz_id INT,
    pin_acesso INT,
    data_criacao TIMESTAMP,
    status VARCHAR(100)
);

CREATE TABLE questoes(
    id INT PRIMARY KEY,
    quiz_id INT,
    enunciado VARCHAR(255),
    tipo_questao VARCHAR(100),
    gabarito VARCHAR(100),
    tempo_limite_seg INT,
    pontos INT
);

CREATE TABLE respostas_alunos(
    id INT PRIMARY KEY,
    aluno_id INT,
    sessao_id INT,
    questao_id INT,
    resposta_submetida VARCHAR(100),
    pontos_obtidos INT,
    data_resposta TIMESTAMP
);

CREATE TABLE feedbacks(
    id INT PRIMARY KEY,
    aluno_id INT,
    sessao_id INT,
    opiniao_dinamica VARCHAR(200),
    duvida_conteudo TEXT
);

/* RELACIONAMENTOS DAS CHAVES ESTRANGEIRAS */

ALTER TABLE quizzes
ADD CONSTRAINT fk_quizzes_professor_id
FOREIGN KEY (professor_id)
REFERENCES usuarios(id);

ALTER TABLE sessoes
ADD CONSTRAINT fk_sessoes_quiz_id
FOREIGN KEY (quiz_id)
REFERENCES quizzes(id);

ALTER TABLE questoes
ADD CONSTRAINT fk_questoes_quiz_id
FOREIGN KEY (quiz_id)
REFERENCES quizzes(id);

ALTER TABLE respostas_alunos
ADD CONSTRAINT fk_respostas_aluno_id
FOREIGN KEY (aluno_id)
REFERENCES usuarios(id);

ALTER TABLE respostas_alunos
ADD CONSTRAINT fk_respostas_sessao_id
FOREIGN KEY (sessao_id)
REFERENCES sessoes(id);

ALTER TABLE respostas_alunos
ADD CONSTRAINT fk_respostas_questao_id
FOREIGN KEY (questao_id)
REFERENCES questoes(id);

ALTER TABLE feedbacks
ADD CONSTRAINT fk_feedbacks_aluno_id
FOREIGN KEY (aluno_id)
REFERENCES usuarios(id);

ALTER TABLE feedbacks
ADD CONSTRAINT fk_feedbacks_sessao_id
FOREIGN KEY (sessao_id)
REFERENCES sessoes(id);