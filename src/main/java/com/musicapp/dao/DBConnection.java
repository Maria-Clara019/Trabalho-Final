package com.musicapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Fornece uma conexão única (singleton) com o banco de dados SQLite e garante
 * a criação das tabelas do sistema, caso ainda não existam.
 */
public final class DBConnection {

    private static final String URL = "jdbc:sqlite:dados/musicapp.db";
    private static Connection conexao;

    private DBConnection() {
    }

    public static Connection getConnection() {
        try {
            if (conexao == null || conexao.isClosed()) {
                java.io.File pasta = new java.io.File("dados");
                if (!pasta.exists()) {
                    pasta.mkdirs();
                }
                Class.forName("org.sqlite.JDBC");
                conexao = DriverManager.getConnection(URL);
                try (Statement st = conexao.createStatement()) {
                    st.execute("PRAGMA foreign_keys = ON");
                }
                criarTabelas(conexao);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
        return conexao;
    }

    private static void criarTabelas(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS artista (" +
                    "id TEXT PRIMARY KEY, " +
                    "nome TEXT NOT NULL)");

            st.execute("CREATE TABLE IF NOT EXISTS album (" +
                    "id TEXT PRIMARY KEY, " +
                    "nome TEXT NOT NULL, " +
                    "ano INTEGER, " +
                    "artista_id TEXT, " +
                    "FOREIGN KEY(artista_id) REFERENCES artista(id))");

            st.execute("CREATE TABLE IF NOT EXISTS musica (" +
                    "id TEXT PRIMARY KEY, " +
                    "titulo TEXT NOT NULL, " +
                    "duracao INTEGER, " +
                    "album_id TEXT, " +
                    "FOREIGN KEY(album_id) REFERENCES album(id))");
        }
    }
}
