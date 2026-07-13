package com.musicapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa um álbum, que possui uma lista de músicas.
 */
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private int anoLancamento;
    private List<Musica> musicas = new ArrayList<>();

    public Album() {
        this.id = UUID.randomUUID().toString();
    }

    public Album(String nome, int anoLancamento) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.anoLancamento = anoLancamento;
    }

    public Album(String id, String nome, int anoLancamento, List<Musica> musicas) {
        this.id = id;
        this.nome = nome;
        this.anoLancamento = anoLancamento;
        this.musicas = musicas != null ? musicas : new ArrayList<>();
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

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public List<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<Musica> musicas) {
        this.musicas = musicas != null ? musicas : new ArrayList<>();
    }

    /** Adiciona uma música ao álbum. */
    public void adicionarMusica(Musica musica) {
        musicas.add(musica);
    }

    /** Remove uma música do álbum. */
    public boolean removerMusica(Musica musica) {
        return musicas.remove(musica);
    }

    /** Remove uma música do álbum pelo título (case insensitive). */
    public boolean removerMusicaPorTitulo(String titulo) {
        return musicas.removeIf(m -> m.getTitulo() != null && m.getTitulo().equalsIgnoreCase(titulo));
    }

    /** Pesquisa uma música do álbum pelo título exato (case insensitive). */
    public Musica pesquisarMusicaPorTitulo(String titulo) {
        for (Musica m : musicas) {
            if (m.getTitulo() != null && m.getTitulo().equalsIgnoreCase(titulo)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return nome + " (" + anoLancamento + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album album = (Album) o;
        return Objects.equals(id, album.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
