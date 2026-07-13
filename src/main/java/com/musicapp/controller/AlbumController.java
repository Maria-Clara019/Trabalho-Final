package com.musicapp.controller;

import com.musicapp.dao.IDAO;
import com.musicapp.dao.album.AlbumFileDAO;
import com.musicapp.dao.album.AlbumSQLiteDAO;
import com.musicapp.model.Album;
import com.musicapp.model.Musica;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller de Álbum (padrão MVC).
 */
public class AlbumController {

    private IDAO<Album, String> dao;

    public AlbumController() {
        this.dao = new AlbumFileDAO();
    }

    public void usarArquivo() {
        this.dao = new AlbumFileDAO();
    }

    public void usarBancoDeDados() {
        this.dao = new AlbumSQLiteDAO();
    }

    public void setDao(IDAO<Album, String> dao) {
        this.dao = dao;
    }

    public void incluir(Album a) {
        validar(a);
        dao.incluir(a);
    }

    public void alterar(Album a) {
        validar(a);
        dao.alterar(a);
    }

    public void excluir(String id) {
        dao.excluir(id);
    }

    public Album consultar(String id) {
        return dao.consultar(id);
    }

    public List<Album> listar() {
        return dao.listar();
    }

    /** Pesquisa álbuns cujo nome contenha o trecho informado. */
    public List<Album> pesquisarPorNome(String trecho) {
        List<Album> resultado = new ArrayList<>();
        String alvo = trecho == null ? "" : trecho.toLowerCase();
        for (Album a : dao.listar()) {
            if (a.getNome() != null && a.getNome().toLowerCase().contains(alvo)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public void adicionarMusica(Album a, Musica m) {
        a.adicionarMusica(m);
        alterar(a);
    }

    public void removerMusica(Album a, Musica m) {
        a.removerMusica(m);
        alterar(a);
    }

    private void validar(Album a) {
        if (a.getNome() == null || a.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do álbum é obrigatório.");
        }
    }
}
