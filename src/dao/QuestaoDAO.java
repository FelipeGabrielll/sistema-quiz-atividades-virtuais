package dao;

import model.Questao;
import model.QuestaoMultiplaEscolha;
import model.QuestaoVerdadeiroFalso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** CRUD da tabela "questoes". A coluna tipo_questao define a subclasse. */
public class QuestaoDAO implements CrudDAO<Questao> {
    public static final String MULTIPLA_ESCOLHA = "MULTIPLA_ESCOLHA";
    public static final String VERDADEIRO_FALSO = "VERDADEIRO_FALSO";

    private static final String COLUNAS =
            "id, quiz_id, enunciado, tipo_questao, gabarito, tempo_limite_seg, pontos";

    @Override
    public Questao inserir(Questao q) throws SQLException {
        String sql = "INSERT INTO questoes (quiz_id, enunciado, tipo_questao, gabarito, tempo_limite_seg, pontos) "
                   + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, q.getQuizId());
            ps.setString(2, q.getEnunciado());
            ps.setString(3, tipoDe(q));
            ps.setString(4, gabaritoDe(q));
            ps.setInt(5, q.getTempoLimiteSeg());
            ps.setInt(6, q.getPontos());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                q.setId(rs.getInt("id"));
            }
        }
        return q;
    }

    @Override
    public Questao buscarPorId(int id) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT " + COLUNAS + " FROM questoes WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Questao> listarTodos() throws SQLException {
        List<Questao> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT " + COLUNAS + " FROM questoes ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Questao> listarPorQuiz(int quizId) throws SQLException {
        List<Questao> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT " + COLUNAS + " FROM questoes WHERE quiz_id = ? ORDER BY id")) {
            ps.setInt(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public boolean atualizar(Questao q) throws SQLException {
        String sql = "UPDATE questoes SET enunciado = ?, gabarito = ?, tempo_limite_seg = ?, pontos = ? WHERE id = ?";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, q.getEnunciado());
            ps.setString(2, gabaritoDe(q));
            ps.setInt(3, q.getTempoLimiteSeg());
            ps.setInt(4, q.getPontos());
            ps.setInt(5, q.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(int id) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM questoes WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static String tipoDe(Questao q) {
        return (q instanceof QuestaoVerdadeiroFalso) ? VERDADEIRO_FALSO : MULTIPLA_ESCOLHA;
    }

    public static String gabaritoDe(Questao q) {
        if (q instanceof QuestaoVerdadeiroFalso vf) {
            if (vf.getRespostaCorreta() == null) return null;
            return vf.getRespostaCorreta() ? "V" : "F"; // Boolean gravado como V/F
        }
        return ((QuestaoMultiplaEscolha) q).getGabarito();
    }

    /** Converte a linha do banco na subclasse correta conforme tipo_questao. */
    private Questao mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int quizId = rs.getInt("quiz_id");
        String enunciado = rs.getString("enunciado");
        String gabarito = rs.getString("gabarito");
        int tempo = rs.getInt("tempo_limite_seg");
        int pontos = rs.getInt("pontos");

        if (VERDADEIRO_FALSO.equals(rs.getString("tipo_questao"))) {
            return new QuestaoVerdadeiroFalso(id, quizId, enunciado, tempo, pontos,
                    QuestaoVerdadeiroFalso.interpretar(gabarito));
        }
        return new QuestaoMultiplaEscolha(id, quizId, enunciado, tempo, pontos, gabarito);
    }
}
