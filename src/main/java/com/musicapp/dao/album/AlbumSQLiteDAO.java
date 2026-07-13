package com.musicapp.dao.album;

import com.musicapp.dao.DBConnection;
import com.musicapp.dao.IDAO;
import com.musicapp.dao.musica.MusicaSQLiteDAO;
import com.musicapp.model.Album;
import com.musicapp.model.Musica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Álbum que persiste no banco de dados SQLite.
 * Mantém sincronizadas, na tabela musica, as músicas pertencentes ao álbum
 * (relacionamento 1:N via coluna album_id).
 */
public class AlbumSQLiteDAO implements IDAO<Album, String> {

    private final MusicaSQLiteDAO musicaDAO = new MusicaSQLiteDAO();

    @Override
    public void incluir(Album a) {
        String sql = "INSERT INTO album (id, nome, ano) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, a.getId());
            ps.setString(2, a.getNome());
            ps.setInt(3, a.getAnoLancamento());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir álbum: " + e.getMessage(), e);
        }
        sincronizarMusicas(a);
    }

    @Override
    public void alterar(Album a) {
        String sql = "UPDATE album SET nome = ?, ano = ? WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setInt(2, a.getAnoLancamento());
            ps.setString(3, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar álbum: " + e.getMessage(), e);
        }
        desvincularMusicasRemovidas(a);
        sincronizarMusicas(a);
    }

    @Override
    public void excluir(String id) {
        try (PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(
                "UPDATE musica SET album_id = NULL WHERE album_id = ?")) {
            ps1.setString(1, id);
            ps1.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desvincular músicas do álbum: " + e.getMessage(), e);
        }
        try (PreparedStatement ps2 = DBConnection.getConnection().prepareStatement(
                "DELETE FROM album WHERE id = ?")) {
            ps2.setString(1, id);
            ps2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir álbum: " + e.getMessage(), e);
        }
    }

    @Override
    public Album consultar(String id) {
        String sql = "SELECT id, nome, ano FROM album WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar álbum: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Album> listar() {
        List<Album> lista = new ArrayList<>();
        String sql = "SELECT id, nome, ano FROM album ORDER BY nome";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar álbuns: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Lista apenas os álbuns vinculados a um determinado artista. */
    public List<Album> listarPorArtista(String artistaId) {
        List<Album> lista = new ArrayList<>();
        String sql = "SELECT id, nome, ano FROM album WHERE artista_id = ? ORDER BY nome";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, artistaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar álbuns do artista: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Associa (ou desassocia, se artistaId for null) um álbum a um artista. */
    public void vincularArtista(String albumId, String artistaId) {
        String sql = "UPDATE album SET artista_id = ? WHERE id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, artistaId);
            ps.setString(2, albumId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao vincular artista: " + e.getMessage(), e);
        }
    }

    private void sincronizarMusicas(Album a) {
        for (Musica m : a.getMusicas()) {
            if (musicaDAO.consultar(m.getId()) == null) {
                musicaDAO.incluir(m);
            } else {
                musicaDAO.alterar(m);
            }
            musicaDAO.vincularAlbum(m.getId(), a.getId());
        }
    }

    private void desvincularMusicasRemovidas(Album a) {
        List<Musica> atuais = musicaDAO.listarPorAlbum(a.getId());
        for (Musica m : atuais) {
            boolean aindaPertence = a.getMusicas().stream().anyMatch(x -> x.getId().equals(m.getId()));
            if (!aindaPertence) {
                musicaDAO.vincularAlbum(m.getId(), null);
            }
        }
    }

    private Album montar(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        return new Album(id, rs.getString("nome"), rs.getInt("ano"), musicaDAO.listarPorAlbum(id));
    }
}
