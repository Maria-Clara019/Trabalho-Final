package com.musicapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa um artista, que possui uma lista de álbuns.
 */
public class Artista implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private List<Album> albuns = new ArrayList<>();

    public Artista() {
        this.id = UUID.randomUUID().toString();
    }

    public Artista(String nome) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
    }

    public Artista(String id, String nome, List<Album> albuns) {
        this.id = id;
        this.nome = nome;
        this.albuns = albuns != null ? albuns : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Album> getAlbuns() {
        return albuns;
    }

    public void setAlbuns(List<Album> albuns) {
        this.albuns = albuns != null ? albuns : new ArrayList<>();
    }

    /** Adiciona um álbum ao artista. */
    public void adicionarAlbum(Album album) {
        albuns.add(album);
    }

    /** Remove um álbum do artista. */
    public boolean removerAlbum(Album album) {
        return albuns.remove(album);
    }

    /** Remove um álbum do artista pelo nome (case insensitive). */
    public boolean removerAlbumPorNome(String nome) {
        return albuns.removeIf(a -> a.getNome() != null && a.getNome().equalsIgnoreCase(nome));
    }

    /** Pesquisa um álbum do artista pelo nome exato (case insensitive). */
    public Album pesquisarAlbumPorNome(String nome) {
        for (Album a : albuns) {
            if (a.getNome() != null && a.getNome().equalsIgnoreCase(nome)) {
                return a;
            }
        }
        return null;
    }

    /** Pesquisa, em todos os álbuns do artista, as músicas com o título informado. */
    public List<Musica> pesquisarMusicaPorTitulo(String titulo) {
        List<Musica> encontradas = new ArrayList<>();
        for (Album a : albuns) {
            Musica m = a.pesquisarMusicaPorTitulo(titulo);
            if (m != null) {
                encontradas.add(m);
            }
        }
        return encontradas;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artista)) return false;
        Artista artista = (Artista) o;
        return Objects.equals(id, artista.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
