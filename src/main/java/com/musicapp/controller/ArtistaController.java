package com.musicapp.controller;

import com.musicapp.dao.IDAO;
import com.musicapp.dao.artista.ArtistaFileDAO;
import com.musicapp.dao.artista.ArtistaSQLiteDAO;
import com.musicapp.model.Album;
import com.musicapp.model.Artista;
import com.musicapp.model.Musica;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller de Artista (padrão MVC).
 */
public class ArtistaController {

    private IDAO<Artista, String> dao;

    public ArtistaController() {
        this.dao = new ArtistaFileDAO();
    }

    public void usarArquivo() {
        this.dao = new ArtistaFileDAO();
    }

    public void usarBancoDeDados() {
        this.dao = new ArtistaSQLiteDAO();
    }

    public void setDao(IDAO<Artista, String> dao) {
        this.dao = dao;
    }

    public void incluir(Artista a) {
        validar(a);
        dao.incluir(a);
    }

    public void alterar(Artista a) {
        validar(a);
        dao.alterar(a);
    }

    public void excluir(String id) {
        dao.excluir(id);
    }

    public Artista consultar(String id) {
        return dao.consultar(id);
    }

    public List<Artista> listar() {
        return dao.listar();
    }

    /** Pesquisa artistas cujo nome contenha o trecho informado. */
    public List<Artista> pesquisarPorNome(String trecho) {
        List<Artista> resultado = new ArrayList<>();
        String alvo = trecho == null ? "" : trecho.toLowerCase();
        for (Artista a : dao.listar()) {
            if (a.getNome() != null && a.getNome().toLowerCase().contains(alvo)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public void adicionarAlbum(Artista a, Album al) {
        a.adicionarAlbum(al);
        alterar(a);
    }

    public void removerAlbum(Artista a, Album al) {
        a.removerAlbum(al);
        alterar(a);
    }

    public Album pesquisarAlbumPorNome(Artista a, String nome) {
        return a.pesquisarAlbumPorNome(nome);
    }

    public List<Musica> pesquisarMusicaPorTitulo(Artista a, String titulo) {
        return a.pesquisarMusicaPorTitulo(titulo);
    }

    private void validar(Artista a) {
        if (a.getNome() == null || a.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do artista é obrigatório.");
        }
    }
}
