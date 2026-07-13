package com.musicapp.dao.musica;

import com.musicapp.dao.DBConnection;
import com.musicapp.dao.IDAO;
import com.musicapp.model.Musica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Música que persiste no banco de dados SQLite.
 */
public class MusicaSQLiteDAO implements IDAO<Musica, String> {

    @Override
    public void incluir(Musica m) {
        String sql = "INSERT INTO musica (id, titulo, duracao) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, m.getId());
            ps.setString(2, m.getTitulo());
            ps.setInt(3, m.getDuracao());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir música: " + e.getMessage(), e);
        }
    }

    @Override
    public void alterar(Musica m) {
        String sql = "UPDATE musica SET titulo = ?, duracao = ? WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, m.getTitulo());
            ps.setInt(2, m.getDuracao());
            ps.setString(3, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar música: " + e.getMessage(), e);
        }
    }

    @Override
    public void excluir(String id) {
        String sql = "DELETE FROM musica WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir música: " + e.getMessage(), e);
        }
    }

    @Override
    public Musica consultar(String id) {
        String sql = "SELECT id, titulo, duracao FROM musica WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar música: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Musica> listar() {
        List<Musica> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, duracao FROM musica ORDER BY titulo";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar músicas: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Lista apenas as músicas vinculadas a um determinado álbum. */
    public List<Musica> listarPorAlbum(String albumId) {
        List<Musica> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, duracao FROM musica WHERE album_id = ? ORDER BY titulo";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, albumId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar músicas do álbum: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Associa (ou desassocia, se albumId for null) uma música a um álbum. */
    public void vincularAlbum(String musicaId, String albumId) {
        String sql = "UPDATE musica SET album_id = ? WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, albumId);
            ps.setString(2, musicaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao vincular música ao álbum: " + e.getMessage(), e);
        }
    }

    private Musica montar(ResultSet rs) throws SQLException {
        return new Musica(rs.getString("id"), rs.getString("titulo"), rs.getInt("duracao"));
    }
}
