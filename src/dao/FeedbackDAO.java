package dao;

import model.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** CRUD da tabela "feedbacks" (RF05). */
public class FeedbackDAO implements CrudDAO<Feedback> {
    private static final String COLUNAS = "id, aluno_id, sessao_id, opiniao_dinamica, duvida_conteudo";

    @Override
    public Feedback inserir(Feedback f) throws SQLException {
        String sql = "INSERT INTO feedbacks (aluno_id, sessao_id, opiniao_dinamica, duvida_conteudo) "
                   + "VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, f.getAlunoId());
            ps.setInt(2, f.getSessaoId());
            ps.setString(3, f.getOpiniaoDinamica());
            ps.setString(4, f.getDuvidaConteudo());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                f.setId(rs.getInt("id"));
            }
        }
        return f;
    }

    @Override
    public Feedback buscarPorId(int id) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT " + COLUNAS + " FROM feedbacks WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Feedback> listarTodos() throws SQLException {
        List<Feedback> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT " + COLUNAS + " FROM feedbacks ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Feedback> listarPorSessao(int sessaoId) throws SQLException {
        List<Feedback> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT " + COLUNAS + " FROM feedbacks WHERE sessao_id = ? ORDER BY id")) {
            ps.setInt(1, sessaoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public boolean atualizar(Feedback f) throws SQLException {
        String sql = "UPDATE feedbacks SET opiniao_dinamica = ?, duvida_conteudo = ? WHERE id = ?";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, f.getOpiniaoDinamica());
            ps.setString(2, f.getDuvidaConteudo());
            ps.setInt(3, f.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(int id) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM feedbacks WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Feedback mapear(ResultSet rs) throws SQLException {
        return new Feedback(rs.getInt("id"), rs.getInt("aluno_id"), rs.getInt("sessao_id"),
                rs.getString("opiniao_dinamica"), rs.getString("duvida_conteudo"));
    }
}
