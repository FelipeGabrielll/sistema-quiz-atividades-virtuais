package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões JDBC com o PostgreSQL (banco "sistema_quiz").
 * Os dados de acesso podem ser trocados pelas variáveis de ambiente
 * DB_URL, DB_USER e DB_PASSWORD, sem recompilar o projeto.
 */
public final class ConexaoBD {
    private static final String URL  = env("DB_URL", "jdbc:postgresql://localhost:5432/sistema_quiz");
    private static final String USER = env("DB_USER", "postgres");
    private static final String PASS = env("DB_PASSWORD", "postgres");

    private ConexaoBD() { }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    private static String env(String nome, String padrao) {
        String valor = System.getenv(nome);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }
}
