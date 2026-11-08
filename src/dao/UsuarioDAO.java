package dao;

import model.Aluno;
import model.Professor;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** CRUD da tabela "usuarios". A coluna tipo_usuario define a subclasse. */
public class UsuarioDAO implements CrudDAO<Usuario> {
    public static final String PROFESSOR = "PROFESSOR";
    public static final String ALUNO = "ALUNO";

    @Override
    public Usuario inserir(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, tipo_usuario) VALUES (?, ?, ?) RETURNING id";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, (u instanceof Professor) ? PROFESSOR : ALUNO);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                u.setId(rs.getInt("id"));
            }
        }
        return u;
    }

    @Override
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, email, tipo_usuario FROM usuarios WHERE id = ?";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nome, email, tipo_usuario FROM usuarios ORDER BY id";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    @Override
    public boolean atualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ? WHERE id = ?";
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setInt(3, u.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(int id) throws SQLException {
        try (Connection c = ConexaoBD.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** Converte a linha do banco na subclasse correta (Professor ou Aluno). */
    private Usuario mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        if (PROFESSOR.equals(rs.getString("tipo_usuario"))) {
            return new Professor(id, nome, email);
        }
        return new Aluno(id, nome, email);
    }
}
