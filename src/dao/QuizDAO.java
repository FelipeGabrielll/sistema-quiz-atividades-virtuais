package dao;

import model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** CRUD da tabela "quizzes" (entidade principal do sistema - RF01). */
public class QuizDAO implements CrudDAO<Quiz> {
    private final QuestaoDAO questaoDAO = new QuestaoDAO();

    @Override
    public Quiz inserir(Quiz q) throws SQLException {
        String sql = "INSERT INTO quizzes (titulo, disciplina, professor_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, q.getTitulo());
            ps.setString(2, q.getDisciplina());
            ps.setInt(3, q.getProfessorId());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                q.setId(rs.getInt("id"));
            }
        }
        return q;
    }

    /** Busca o quiz já com suas questões carregadas (composição 1:N). */
    @Override
    public Quiz buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, titulo, disciplina, professor_id FROM quizzes WHERE id = ?";
        Quiz quiz = null;
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) quiz = mapear(rs);
            }
        }
        if (quiz != null) carregarQuestoes(quiz);
        return quiz;
    }

    @Override
    public List<Quiz> listarTodos() throws SQLException {
        List<Quiz> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, disciplina, professor_id FROM quizzes ORDER BY id";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        for (Quiz q : lista) carregarQuestoes(q);
        return lista;
    }

    @Override
    public boolean atualizar(Quiz q) throws SQLException {
        String sql = "UPDATE quizzes SET titulo = ?, disciplina = ? WHERE id = ?";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, q.getTitulo());
            ps.setString(2, q.getDisciplina());
            ps.setInt(3, q.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /** As questões do quiz são excluídas junto (ON DELETE CASCADE no schema.sql). */
    @Override
    public boolean excluir(int id) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM quizzes WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private void carregarQuestoes(Quiz quiz) throws SQLException {
        questaoDAO.listarPorQuiz(quiz.getId()).forEach(quiz::adicionarQuestao);
    }

    private Quiz mapear(ResultSet rs) throws SQLException {
        return new Quiz(rs.getInt("id"), rs.getString("titulo"),
                rs.getString("disciplina"), rs.getInt("professor_id"));
    }
}
