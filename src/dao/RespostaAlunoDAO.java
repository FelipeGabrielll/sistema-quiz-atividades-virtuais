package dao;

import model.RespostaAluno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** CRUD da tabela "respostas_alunos" + consultas de ranking (RF03) e resultados (RF04). */
public class RespostaAlunoDAO implements CrudDAO<RespostaAluno> {
    private static final String COLUNAS =
            "id, aluno_id, sessao_id, questao_id, resposta_submetida, pontos_obtidos, data_resposta";

    /** Linha do ranking: nome do aluno e total de pontos na sessão. */
    public record LinhaRanking(String aluno, int pontos) { }

    /** Linha do painel de resultados: acertos e erros por disciplina. */
    public record LinhaResultado(String disciplina, int acertos, int erros) { }

    @Override
    public RespostaAluno inserir(RespostaAluno r) throws SQLException {
        String sql = "INSERT INTO respostas_alunos (aluno_id, sessao_id, questao_id, resposta_submetida, "
                   + "pontos_obtidos, data_resposta) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getAlunoId());
            ps.setInt(2, r.getSessaoId());
            ps.setInt(3, r.getQuestaoId());
            ps.setString(4, r.getRespostaSubmetida());
            ps.setInt(5, r.getPontosObtidos());
            ps.setTimestamp(6, new Timestamp(r.getDataResposta().getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                r.setId(rs.getInt("id"));
            }
        }
        return r;
    }

    @Override
    public RespostaAluno buscarPorId(int id) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT " + COLUNAS + " FROM respostas_alunos WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<RespostaAluno> listarTodos() throws SQLException {
        List<RespostaAluno> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT " + COLUNAS + " FROM respostas_alunos ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    @Override
    public boolean atualizar(RespostaAluno r) throws SQLException {
        String sql = "UPDATE respostas_alunos SET resposta_submetida = ?, pontos_obtidos = ? WHERE id = ?";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getRespostaSubmetida());
            ps.setInt(2, r.getPontosObtidos());
            ps.setInt(3, r.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(int id) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM respostas_alunos WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean jaRespondeu(int alunoId, int sessaoId, int questaoId) throws SQLException {
        String sql = "SELECT 1 FROM respostas_alunos WHERE aluno_id = ? AND sessao_id = ? AND questao_id = ?";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, alunoId);
            ps.setInt(2, sessaoId);
            ps.setInt(3, questaoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Ranking da sessão: soma de pontos por aluno, do maior para o menor (RF03). */
    public List<LinhaRanking> ranking(int sessaoId) throws SQLException {
        String sql = "SELECT u.nome, SUM(r.pontos_obtidos) AS total "
                   + "FROM respostas_alunos r JOIN usuarios u ON u.id = r.aluno_id "
                   + "WHERE r.sessao_id = ? GROUP BY u.nome ORDER BY total DESC, u.nome";
        List<LinhaRanking> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sessaoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(new LinhaRanking(rs.getString("nome"), rs.getInt("total")));
            }
        }
        return lista;
    }

    /** Painel de resultados: acertos e erros por disciplina dos quizzes do professor (RF04). */
    public List<LinhaResultado> resultadosPorDisciplina(int professorId) throws SQLException {
        String sql = "SELECT qz.disciplina, "
                   + "SUM(CASE WHEN r.pontos_obtidos > 0 THEN 1 ELSE 0 END) AS acertos, "
                   + "SUM(CASE WHEN r.pontos_obtidos > 0 THEN 0 ELSE 1 END) AS erros "
                   + "FROM respostas_alunos r "
                   + "JOIN questoes q ON q.id = r.questao_id "
                   + "JOIN quizzes qz ON qz.id = q.quiz_id "
                   + "WHERE qz.professor_id = ? "
                   + "GROUP BY qz.disciplina ORDER BY qz.disciplina";
        List<LinhaResultado> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, professorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new LinhaResultado(rs.getString("disciplina"),
                            rs.getInt("acertos"), rs.getInt("erros")));
                }
            }
        }
        return lista;
    }

    private RespostaAluno mapear(ResultSet rs) throws SQLException {
        return new RespostaAluno(rs.getInt("id"), rs.getInt("aluno_id"), rs.getInt("sessao_id"),
                rs.getInt("questao_id"), rs.getString("resposta_submetida"),
                rs.getInt("pontos_obtidos"), rs.getTimestamp("data_resposta"));
    }
}
