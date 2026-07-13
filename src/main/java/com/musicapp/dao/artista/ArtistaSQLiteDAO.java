package com.musicapp.dao.artista;

import com.musicapp.dao.DBConnection;
import com.musicapp.dao.IDAO;
import com.musicapp.dao.album.AlbumSQLiteDAO;
import com.musicapp.model.Album;
import com.musicapp.model.Artista;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Artista que persiste no banco de dados SQLite.
 * Mantém sincronizados, na tabela album, os álbuns pertencentes ao artista
 * (relacionamento 1:N via coluna artista_id).
 */
public class ArtistaSQLiteDAO implements IDAO<Artista, String> {

    private final AlbumSQLiteDAO albumDAO = new AlbumSQLiteDAO();

    @Override
    public void incluir(Artista a) {
        String sql = "INSERT INTO artista (id, nome) VALUES (?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, a.getId());
            ps.setString(2, a.getNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir artista: " + e.getMessage(), e);
        }
        sincronizarAlbuns(a);
    }

    @Override
    public void alterar(Artista a) {
        String sql = "UPDATE artista SET nome = ? WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar artista: " + e.getMessage(), e);
        }
        desvincularAlbunsRemovidos(a);
        sincronizarAlbuns(a);
    }

    @Override
    public void excluir(String id) {
        try (PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(
                "UPDATE album SET artista_id = NULL WHERE artista_id = ?")) {
            ps1.setString(1, id);
            ps1.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desvincular álbuns do artista: " + e.getMessage(), e);
        }
        try (PreparedStatement ps2 = DBConnection.getConnection().prepareStatement(
                "DELETE FROM artista WHERE id = ?")) {
            ps2.setString(1, id);
            ps2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir artista: " + e.getMessage(), e);
        }
    }

    @Override
    public Artista consultar(String id) {
        String sql = "SELECT id, nome FROM artista WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar artista: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Artista> listar() {
        List<Artista> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM artista ORDER BY nome";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar artistas: " + e.getMessage(), e);
        }
        return lista;
    }

    private void sincronizarAlbuns(Artista a) {
        for (Album al : a.getAlbuns()) {
            if (albumDAO.consultar(al.getId()) == null) {
                albumDAO.incluir(al);
            } else {
                albumDAO.alterar(al);
            }
            albumDAO.vincularArtista(al.getId(), a.getId());
        }
    }

    private void desvincularAlbunsRemovidos(Artista a) {
        List<Album> atuais = albumDAO.listarPorArtista(a.getId());
        for (Album al : atuais) {
            boolean aindaPertence = a.getAlbuns().stream().anyMatch(x -> x.getId().equals(al.getId()));
            if (!aindaPertence) {
                albumDAO.vincularArtista(al.getId(), null);
            }
        }
    }

    private Artista montar(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        return new Artista(id, rs.getString("nome"), albumDAO.listarPorArtista(id));
    }
}
