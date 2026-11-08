package service;

import dao.QuestaoDAO;
import dao.QuizDAO;
import dao.UsuarioDAO;
import model.Professor;
import model.Questao;
import model.QuestaoVerdadeiroFalso;
import model.Quiz;
import model.Usuario;

import java.sql.SQLException;
import java.util.List;

/** Regras de negócio do RF01: criar, editar e estruturar quizzes e suas questões. */
public class QuizService {
    private final QuizDAO quizDAO = new QuizDAO();
    private final QuestaoDAO questaoDAO = new QuestaoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // ---------- Quiz ----------
    public Quiz criarQuiz(int professorId, String titulo, String disciplina) throws SQLException {
        Usuario dono = usuarioDAO.buscarPorId(professorId);
        if (!(dono instanceof Professor professor)) {
            throw new IllegalArgumentException("Somente um professor cadastrado pode criar quizzes.");
        }
        Quiz quiz = professor.criarQuiz(titulo, disciplina);
        validarQuiz(quiz);
        return quizDAO.inserir(quiz);
    }

    public Quiz buscarQuiz(int id) throws SQLException {
        return quizDAO.buscarPorId(id);
    }

    public List<Quiz> listarQuizzes() throws SQLException {
        return quizDAO.listarTodos();
    }

    public boolean atualizarQuiz(Quiz quiz) throws SQLException {
        validarQuiz(quiz);
        return quizDAO.atualizar(quiz);
    }

    public boolean excluirQuiz(int id) throws SQLException {
        return quizDAO.excluir(id);
    }

    // ---------- Questões do quiz ----------
    public Questao adicionarQuestao(Quiz quiz, Questao questao) throws SQLException {
        validarQuestao(questao);
        quiz.adicionarQuestao(questao);
        return questaoDAO.inserir(questao);
    }

    public Questao buscarQuestao(int id) throws SQLException {
        return questaoDAO.buscarPorId(id);
    }

    public boolean atualizarQuestao(Questao questao) throws SQLException {
        validarQuestao(questao);
        return questaoDAO.atualizar(questao);
    }

    public boolean excluirQuestao(int id) throws SQLException {
        return questaoDAO.excluir(id);
    }

    // ---------- Validações ----------
    private void validarQuiz(Quiz quiz) {
        if (quiz.getTitulo() == null || quiz.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título do quiz é obrigatório.");
        }
        if (quiz.getDisciplina() == null || quiz.getDisciplina().isBlank()) {
            throw new IllegalArgumentException("A disciplina do quiz é obrigatória.");
        }
    }

    private void validarQuestao(Questao q) {
        if (q.getEnunciado() == null || q.getEnunciado().isBlank()) {
            throw new IllegalArgumentException("O enunciado é obrigatório.");
        }
        if (q.getTempoLimiteSeg() == null || q.getTempoLimiteSeg() <= 0) {
            throw new IllegalArgumentException("O tempo limite deve ser maior que zero.");
        }
        if (q.getPontos() == null || q.getPontos() < 0) {
            throw new IllegalArgumentException("A pontuação não pode ser negativa.");
        }
        String gabarito = QuestaoDAO.gabaritoDe(q);
        if (gabarito == null || gabarito.isBlank()) {
            throw new IllegalArgumentException(q instanceof QuestaoVerdadeiroFalso
                    ? "Questões V/F aceitam apenas gabarito V ou F."
                    : "O gabarito é obrigatório.");
        }
    }
}
