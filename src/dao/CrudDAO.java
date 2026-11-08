package dao;

import java.sql.SQLException;
import java.util.List;

/** Contrato genérico de CRUD implementado por todos os DAOs. */
public interface CrudDAO<T> {
    T inserir(T entidade) throws SQLException;        // Create
    T buscarPorId(int id) throws SQLException;        // Read (um) - null se não existir
    List<T> listarTodos() throws SQLException;        // Read (todos)
    boolean atualizar(T entidade) throws SQLException; // Update
    boolean excluir(int id) throws SQLException;      // Delete
}
