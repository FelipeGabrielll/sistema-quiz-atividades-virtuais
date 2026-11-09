package dao;

import model.Sessao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** CRUD da tabela "sessoes". */
public class SessaoDAO implements CrudDAO<Sessao> {
    private static final String COLUNAS = "id, quiz_id, pin_acesso, data_criacao, status";

    @Override
    public Sessao inserir(Sessao s) throws SQLException {
        String sql = "INSERT INTO sessoes (quiz_id, pin_acesso, data_criacao, status) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getQuizId());
            ps.setInt(2, s.getPinAcesso());
            ps.setTimestamp(3, new Timestamp(s.getDataCriacao().getTime()));
            ps.setString(4, s.getStatus());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                s.setId(rs.getInt("id"));
            }
        }
        return s;
    }

    @Override
    public Sessao buscarPorId(int id) throws SQLException {
        return buscarUma("SELECT " + COLUNAS + " FROM sessoes WHERE id = ?", id);
    }

    /** Localiza a sessão ABERTA com o PIN informado (login da turma). */
    public Sessao buscarAbertaPorPin(int pin) throws SQLException {
        return buscarUma("SELECT " + COLUNAS + " FROM sessoes WHERE pin_acesso = ? AND status = 'ABERTA'", pin);
    }

    /** Sessão mais recente com o PIN informado (aberta ou encerrada). */
    public Sessao buscarPorPin(int pin) throws SQLException {
        return buscarUma("SELECT " + COLUNAS + " FROM sessoes WHERE pin_acesso = ? ORDER BY id DESC LIMIT 1", pin);
    }

    public boolean existePinAberto(int pin) throws SQLException {
        return buscarAbertaPorPin(pin) != null;
    }

    @Override
    public List<Sessao> listarTodos() throws SQLException {
        List<Sessao> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT " + COLUNAS + " FROM sessoes ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    @Override
    public boolean atualizar(Sessao s) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("UPDATE sessoes SET status = ? WHERE id = ?")) {
            ps.setString(1, s.getStatus());
            ps.setInt(2, s.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /** Exclui a sessão junto com suas respostas e feedbacks, numa única transação. */
    @Override
    public boolean excluir(int id) throws SQLException {
        try (Connection c = ConexaoBD.getConexao()) {
            c.setAutoCommit(false);
            try (PreparedStatement r = c.prepareStatement("DELETE FROM respostas_alunos WHERE sessao_id = ?");
                 PreparedStatement f = c.prepareStatement("DELETE FROM feedbacks WHERE sessao_id = ?");
                 PreparedStatement s = c.prepareStatement("DELETE FROM sessoes WHERE id = ?")) {
                r.setInt(1, id); r.executeUpdate();
                f.setInt(1, id); f.executeUpdate();
                s.setInt(1, id);
                boolean excluiu = s.executeUpdate() > 0;
                c.commit();
                return excluiu;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    private Sessao buscarUma(String sql, int parametro) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private Sessao mapear(ResultSet rs) throws SQLException {
        return new Sessao(rs.getInt("id"), rs.getInt("quiz_id"), rs.getInt("pin_acesso"),
                rs.getString("status"), rs.getTimestamp("data_criacao"));
    }
}
