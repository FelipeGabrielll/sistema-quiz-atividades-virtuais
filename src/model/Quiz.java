package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Composição 1:N com Questao: um Quiz é formado por várias questões,
 * que não existem sem ele (no banco: fk_questoes_quiz_id ON DELETE CASCADE).
 */
public class Quiz {
    private Integer id;
    private String titulo;
    private String disciplina;
    private Integer professorId;
    private List<Questao> questoes = new ArrayList<>();

    public Quiz(Integer id, String titulo, String disciplina, Integer professorId) {
        this.id = id;
        this.titulo = titulo;
        this.disciplina = disciplina;
        this.professorId = professorId;
    }

    public void adicionarQuestao(Questao q) {
        q.setQuizId(this.id);
        this.questoes.add(q);
    }

    public List<Questao> getQuestoes() { return questoes; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }

    public Integer getProfessorId() { return professorId; }
    public void setProfessorId(Integer professorId) { this.professorId = professorId; }
}
