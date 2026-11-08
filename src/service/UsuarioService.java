package service;

import dao.UsuarioDAO;
import model.Usuario;

import java.sql.SQLException;
import java.util.List;

/** Regras de negócio de usuários (professores e alunos). */
public class UsuarioService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario cadastrar(Usuario u) throws SQLException {
        validar(u);
        return usuarioDAO.inserir(u);
    }

    public Usuario buscar(int id) throws SQLException {
        return usuarioDAO.buscarPorId(id);
    }

    public List<Usuario> listar() throws SQLException {
        return usuarioDAO.listarTodos();
    }

    public boolean atualizar(Usuario u) throws SQLException {
        validar(u);
        return usuarioDAO.atualizar(u);
    }

    public boolean excluir(int id) throws SQLException {
        return usuarioDAO.excluir(id);
    }

    private void validar(Usuario u) {
        if (u.getNome() == null || u.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        if (u.getEmail() == null || !u.getEmail().contains("@")) {
            throw new IllegalArgumentException("Informe um e-mail válido.");
        }
    }
}
